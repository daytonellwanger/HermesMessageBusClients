# Running the Client

- Install node dependencies (note: you may need to have admin privileges) - `npm install`
- Run the client - `node FacebookClient.js`

You should see
```
18
<TAG>FACEBOOK_POST
```
output to the console.

To test making a post, enter the following in the console:
```
56
{"tags":["FACEBOOK_POST"],"postMessage":"Hello, world!"}
```

Note: you may receive an error about an invalid access token. In this case, you need to get a new access token from [Facebook's graph explorer](https://developers.facebook.com/tools/explorer/). After receiving a new token, update `info.json`.