var readline = require('readline');
var AppsScript = require('./AppsScriptUtils');
var ClientThread = require("./MessageBusClientThread");
var JsDiff = require('diff');

var BASE_FOLDER = "hermes";
var TAGS_FIELD = "tags";
var EDITOR_CONTENTS_TAG = "EDITOR_CONTENTS";
var EDITOR_CHANGE_TAG = "EDITOR_CHANGE";
var APPEND_TAG = "APPEND";
var UPDATE_PERMISSIONS_TAG = "UPDATE_PERMISSIONS";
var FILE_DATA_REQUEST_TAG = "FILE_DATA_REQUEST";
var TAGS = APPEND_TAG + "|" +
            EDITOR_CONTENTS_TAG + "|"
            + EDITOR_CHANGE_TAG + "|"
            + UPDATE_PERMISSIONS_TAG + "|"
			+ FILE_DATA_REQUEST_TAG ;

var activeFiles = {};

AppsScript.initialize(() => {
    ClientThread.sendTags(TAGS);
    ClientThread.setInputFinishedCallback(receiveMessage);
});

function receiveMessage(message) {
    var json = JSON.parse(message);
    var tags = json[TAGS_FIELD];
    if(containsTag(tags, EDITOR_CONTENTS_TAG)) {
        ClientThread.output("Got editor contents message");
        editorContents(json);
    }
    if(containsTag(tags, EDITOR_CHANGE_TAG)) {
        ClientThread.output("Got editor change message");
        editorChange(json);
    }
    if(containsTag(tags, APPEND_TAG)) {
        ClientThread.output("Got append message");
        editorAppend(json);
    }
    if(containsTag(tags, UPDATE_PERMISSIONS_TAG)) {
        ClientThread.output("Got update permissions");
    }
	if(containsTag(tags,FILE_DATA_REQUEST_TAG)) {
        ClientThread.output("Got fle data request");
		getFileData(json);
    }
}

function containsTag(tagArray, tag) {
    return (tagArray.indexOf(tag) != -1);
}

function getFullFilename(from, filename) {
    return BASE_FOLDER + "/" + from + "/" + filename;
}

function editorContents(json) {
    if(checkJSONProperties(json, ["filename", "contents", "isPublic", "from"])) {
        var filename = getFullFilename(json["from"], json["filename"]);
        pushEditorContents(filename, json["from"], json["contents"], json["isPublic"]);
    }
}

function editorChange(json) {
    if(checkJSONProperties(json, ["filename", "from", "offset", "length", "text"])) {
        var filename = getFullFilename(json["from"], json["filename"]);
        pushEditorChange(filename, json["from"], json["offset"], json["length"], json["text"]);
     }
}

function editorAppend(json) {
    if(checkJSONProperties(json, ["filename", "from", "append"])) {
        var filename = getFullFilename(json["from"], json["filename"]);
        AppsScript.append(filename, json["append"], (result) => {
            ClientThread.output("Appended");
        });
    }
}

function updatePermisisons(json) {
    if(checkJSONProperties(json, ["filename", "from", "isPublic"])) {
        var filename = getFullFilename(json["from"], json["filename"]);
        AppsScript.changeFilePermissions(filename, json["isPublic"], (result) => {
            ClientThread.output("Permissions changed.");
        });
    }
}

function getFileData(json) {
    if(checkJSONProperties(json, ["filename", "from"])) {

        var filename = getFullFilename(json["from"], json["filename"]);

        AppsScript.getDocUrl(filename, (result) => {
            ClientThread.output(result);
			if (checkJSONProperties(json, ["callbackTag"])) {
				var aReturnMessage = {};
				aReturnMessage["tags"] = [json["callbackTag"]];
				aReturnMessage["fileUrl"] = result;
				if (checkJSONProperties(json, ["source"])) {
					aReturnMessage["source"] = json["source"];
				}
				ClientThread.sendMessage(JSON.stringify(aReturnMessage));
			}
        });
    }
}

function pushEditorContents(filename, from, contents, public) {
    makeFileRequest(from, () => {
        ClientThread.output("Calling AppScripts");
        /*AppScripts.newDocumentContent(filename, contents, public, null, "", (result) => {
            fileOperationComplete(from, result);
        });*/
        AppsScript.getDocText(filename, (result) => {
            diff = computeDiff(result, contents);
            AppsScript.applyDiff(filename, diff, public, (result) => {
                fileOperationComplete(from, result);
            });
        });
    });
}

function computeDiff(s1, s2) {
    var diff = JsDiff.diffChars(s1, s2);
    var changes = [];
    var docLoc = 0;
    diff.forEach((part) => {
        if(part.added) {
            change = {
                type: 'a',
                content: part.value,
                idx: docLoc
            };
            docLoc += part.count;
            changes.push(change);
        } else if(part.removed) {
            change = {
                type: 'd',
                idx: docLoc,
                count: part.count
            }
            changes.push(change);
        } else {
            docLoc += part.count;
        }
    });
    return changes;
}

function pushEditorChange(filename, from, offset, length, text) {
    makeFileRequest(from, () => {
        ClientThread.output("Calling AppScripts");
        AppsScript.replaceText(filename, offset, length, text, null, "", (result) => {
            fileOperationComplete(from, result);
        });
    });
}

function makeFileRequest(filename, fn) {
    if(activeFiles.hasOwnProperty(filename)) {
        activeFiles[filename].push(fn);
    } else {
        activeFiles[filename] = [fn];
        fn();
    }
}

function fileOperationComplete(from, result) {
    ClientThread.output(result);
    activeFiles[from].shift();
    if(activeFiles[from].length > 0) {
        activeFiles[from][0]();
    } else {
        delete activeFiles[from];
    }
}

function checkJSONProperties(json, properties) {
    for(var i = 0; i < properties.length; i++) {
        if(!json.hasOwnProperty(properties[i])) {
            return false;
        }
    }
    return true;
}