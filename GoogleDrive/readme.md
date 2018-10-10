# Running the Client

- Install node dependencies (note: you may need to have admin privileges) - `npm install`

- Run the client - `node GoogleDriveClient.js`

The first time you do this, you'll be prompted to go through an OAuth flow. The account you sign in with is the one that the Google Drive client will use. This will generate a file `script-nodejs-quickstart.json` in `.credentials` in your user directory (on Windows `C:\Users\YourName`).

You should see
```
78
<TAG>APPEND|EDITOR_CONTENTS|EDITOR_CHANGE|UPDATE_PERMISSIONS|FILE_DATA_REQUEST
```
output to the console.