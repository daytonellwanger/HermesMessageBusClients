var readline = require('readline');

var TAG_START = "<TAG>";
var MSG_START = "<MSG>";
var OUTPUT_START = "<OUTPUT>";

var currentInput;
var remainingChars = 0;
var inputFinishedCallback;

module.exports = {};

var rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
rl.on('line', lineRead);

function lineRead(line) {
    if(remainingChars == 0) {
        currentInput = "";
        remainingChars = parseInt(line);
    } else {
        currentInput += line;
        remainingChars -= line.length;
        if(remainingChars > 0) {
            currentInput += "\n";
            remainingChars--;
        }
        if(remainingChars <= 0) {
            inputFinished();
        }
    }
}

function inputFinished() {
    inputFinishedCallback(currentInput);
}

module.exports.setInputFinishedCallback = function(newInputFinishedCallback) {
    inputFinishedCallback = newInputFinishedCallback;
}

function sendToCentral(startString, content) {
    var fullMessage = startString + content;
    var messageLength = fullMessage.length;
    console.log(messageLength);
    console.log(fullMessage);
}

module.exports.sendTags = function(tagsRegex) {
    sendToCentral(TAG_START, tagsRegex);
}

module.exports.sendMessage = function(message) {
    sendToCentral(MSG_START, message);
}

module.exports.output = function(output) {
    sendToCentral(OUTPUT_START, output);
}