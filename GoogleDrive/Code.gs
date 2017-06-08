function getDoc(path) {
  var tokens = path.split("/");
  var fileName = tokens.pop();
  path = path.substring(0, path.length - fileName.length - 1);
  var folder = getFolder(path);
  var files = folder.getFilesByName(fileName);
  var doc;
  if(files.hasNext()) {
    doc = DocumentApp.openById(files.next().getId());
  } else {
    doc = DocumentApp.create(fileName);
    var file = DriveApp.getFileById(doc.getId());
    folder.addFile(file);
    DriveApp.getRootFolder().removeFile(file);
  }
  return doc;
}

function getFile(path) {
  var tokens = path.split("/");
  var fileName = tokens.pop();
  path = path.substring(0, path.length - fileName.length - 1);
  var folder = getFolder(path);
  var files = folder.getFilesByName(fileName);
  if(files.hasNext()) {
    return files.next();
  } else {
    doc = DocumentApp.create(fileName);
    var file = DriveApp.getFileById(doc.getId());
    folder.addFile(file);
    DriveApp.getRootFolder().removeFile(file);
    return file;
  }
}

function changeFilePermissions(path, public) {
  var file = getFile(path);
  if(public) {
    if(file.getSharingAccess() != DriveApp.Access.ANYONE) {
      file.setSharing(DriveApp.Access.ANYONE, DriveApp.Permission.VIEW);
    }
  } else {
    if(file.getSharingAccess() != DriveApp.Access.PRIVATE) {
      file.setSharing(DriveApp.Access.PRIVATE, DriveApp.Permission.VIEW);
    }
  }              
}

function getFolder(path, parentFolder) {
  if(!parentFolder) {
    parentFolder = DriveApp.getRootFolder();
  }
  if(path.length === 0) {
    return parentFolder;
  }
  var folderNames = path.split("/");
  var currentFolderName = folderNames[0];
  var folders = parentFolder.getFoldersByName(currentFolderName);
  var currentFolder;
  if(folders.hasNext()) {
    currentFolder = folders.next();
  } else {
    currentFolder = parentFolder.createFolder(currentFolderName);
  }
  var subPath = path.substring(currentFolderName.length + 1, path.length);
  return getFolder(subPath, currentFolder);
}

function getDocUrl(path) {
  return getDoc(path).getUrl();
}

function getFolderUrl(path) {
  return getFolder(path).getUrl();
}

/*function replaceText(docPath, textChanges, oldFooter, newFooter) {
  var doc = getDoc(docPath);
  if(oldFooter) {
    if(doc.getFooter().getText() != oldFooter) {
      return false;
    }
  }
  var docText = doc.getBody().editAsText();
  for(var i = 0; i < textChanges.length; i++) {
    var offset = textChanges[i][0];
    var length = textChanges[i][1];
    var text = textChanges[i][2];
    if(length > 0) {
      docText.deleteText(offset, offset+length-1);
    }
    if(text == "#newline#") {
      docText.insertText(offset, "\r\n");
    } else {
      docText.insertText(offset, text);
    }
  }
  doc.getFooter().setText(newFooter);
  return true;
}*/

function replaceText(docPath, offset, length, text/*, oldFooter, newFooter*/) {
  var doc = getDoc(docPath);
  /*if(oldFooter) {
    if(doc.getFooter().getText() != oldFooter) {
      return false;
    }
  }*/
  var docText = doc.getBody().editAsText();
  if(length > 0) {
    docText.deleteText(offset, offset+length-1);
  }
  docText.insertText(offset, text);
  //doc.getFooter().setText(newFooter);
  return true;
}

function doDiff() {
  var diffs = [{type: 'a', content: 'abc', idx: 0}];
  applyDiff("/hermes/dayton2/difffile", diffs, false);
}

function applyDiff(docPath, diffList, public) {
  var docText = getDoc(docPath).getBody().editAsText();
  for(var i = 0; i < diffList.length; i++) {
    var diff = diffList[i];
    if(diff["type"] == 'a') {
      docText.insertText(diff["idx"], diff["content"]);
    }
    if(diff["type"] == 'd') {
      docText.deleteText(diff["idx"], diff["idx"] + diff["count"] - 1);
    }
  }
  changeFilePermissions(docPath, public);
}

function newDocumentContent(docPath, content, public/*, oldFooter, newFooter*/) {
  var doc = getDoc(docPath);
  /*if(oldFooter) {
    if(doc.getFooter().getText() != oldFooter) {
      return false;
    }
  }*/
  var docText = doc.getBody().getText();
  if(content != docText) {
    doc.getBody().setText(content);
  }
  
  changeFilePermissions(docPath, public);
  
  //doc.getFooter().setText(newFooter);
  return true;
}

function getDocText(docPath) {
  return getDoc(docPath).getBody().getText();
}

function append(docPath, content) {
  var doc = getDoc(docPath);
  doc.appendParagraph(content);
  return true;
}
