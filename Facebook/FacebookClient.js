var graph = require('fbgraph');
var ClientThread = require("./MessageBusClientThread");

var TAGS_FIELD = "tags";
var POST_TAG = "FACEBOOK_POST";
var TAGS = POST_TAG;

//var token = 'EAAGZACZCCxKNYBAMoCwHteZB2HXlGmXgJ7c6C3kSDykLiaaZBiGTKWELIYlL9hZARcSPzCTZAeQ0MgkQoxtdUobN0TEppvaSn9ltQMohjJryd1A5nEjGNxUabH0KqBVst4h41iZAXqeX9UKZAsj5Qe3QxqR3ICcYcE7ALZCvAzipk4gZDZD';
var token = 'EAAbFdc3jhRwBAFZBCCslLXc3480VgFTZBO5rOpVoNAlDeTdM7OhLEkYJaRnDgSoZCI2SLX2whtfTAtdkC5fSgG03oLPlZBBlpKjG1T8rsJN5n3VyulzfdLIdRkg3FKWEB26dUAa7jYKYglz7B0JZBL5ViCZA5tU9odyzdb1fVDmhmpELCnt9ybkRgWqcwe1kcZD';
graph.setAccessToken(token);

ClientThread.sendTags(TAGS);
ClientThread.setInputFinishedCallback(receiveMessage);

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

function post(json) {
    if(checkJSONProperties(json, ["postMessage"])) {
        var wallPost = {
            message: json["postMessage"]
        };
        graph.post("2220977201461279/feed", wallPost, function(err, res) {
            ClientThread.output("Error: " + err);
            ClientThread.output("Res: " + res);
        });
    }
}


