var graph = require('fbgraph');
var ClientThread = require("./MessageBusClientThread");
var fs = require('fs');

var TAGS_FIELD = "tags";
var POST_TAG = "FACEBOOK_POST";
var TAGS = POST_TAG;

var refreshTime = 60 * 1000 *60*24*5; //Refresh after 5 days.

var infoObject;
var token;
var pageID;
var appID;
var appSecret;
var lastRefresh;

fs.readFile('info.json', processInfo);

function processInfo(err, content) {
    if (err) {
      console.log('Error loading file: ' + err);
      return;
    }

    infoObject = JSON.parse(content);
    token = infoObject["token"];
    pageID = infoObject["pageID"];
    appID = infoObject["appID"];
    appSecret = infoObject["appSecret"];
    lastRefresh = new Date(infoObject["lastRefresh"]);

    graph.setAccessToken(token);
    ClientThread.sendTags(TAGS);
    ClientThread.setInputFinishedCallback(receiveMessage);
}

function receiveMessage(message) { 
    var json = JSON.parse(message);
    var tags = json[TAGS_FIELD];
    if(containsTag(tags, POST_TAG)) {
        ClientThread.output("Got post message");
        post(json);
    }
}

function containsTag(tagArray, tag) {
    return (tagArray.indexOf(tag) != -1);
}

function checkJSONProperties(json, properties) {
    for(var i = 0; i < properties.length; i++) {
        if(!json.hasOwnProperty(properties[i])) {
            return false;
        }
    }
    return true;
}

function checkLastRefresh() {
    var currentDate = new Date();
    var diff = currentDate.getTime() - lastRefresh.getTime();
    if(diff > refreshTime) {
        extendToken();
    }
}

function extendToken() {
    graph.extendAccessToken({
        "client_id": appID,
        "client_secret":  appSecret
    }, function (err, facebookRes) {
        if(err) {
            console.log(err);
        } else {
            lastRefresh = new Date();
            infoObject["lastRefresh"] = lastRefresh;
            infoObject["token"] = facebookRes["access_token"];
            saveInfo();
        }
    });
}

function saveInfo() {
    fs.writeFile("info.json", JSON.stringify(infoObject));
}

function post(json) {
    checkLastRefresh();
    if(checkJSONProperties(json, ["postMessage"])) {
        var wallPost = {
            message: json["postMessage"]
        };
        graph.post(pageID, wallPost, function(err, res) {
            ClientThread.output("Error: " + err);
            ClientThread.output("Res: " + res);
        });
    }
}