
const express = require('express');
const bodyParser = require('body-parser')
const renderer = require('vue-server-renderer').createRenderer({
	
});
const createApp = require('./src/app');
const server = express();
server.use(bodyParser.json());

server.post('/process', (req, res) => {
	let jsonBody = req.body;
	// console.log(JSON.stringify(jsonBody))
  if (jsonBody == undefined || Object.keys(jsonBody).length === 0){
    return res.status(400).send('Request not found');
  }
	console.log("@@ NodeServer: received request: \n")
	console.log("@@ NodeServer: template : \n"+jsonBody.template)
	console.log("@@ NodeServer: data : \n"+JSON.stringify(jsonBody.data))
	if (jsonBody.template == undefined) {
	console.log("@@ NodeServer: missing template")
		return res.status(400).send('Missing template');
	}
	if (jsonBody.data == undefined || Object.keys(jsonBody.data).length === 0) {
	console.log("@@ NodeServer: missing data")
		return res.status(400).send('Missing data');
	}

	try {
	const app = createApp(
		jsonBody.template, 
		jsonBody.data);


	const context = {
		title: "Vue SSR Tutorial"
	};

	renderer.renderToString(app, context, (err, html) => {
		if (err) {

					console.log(err.message);

			console.log("@@ NodeServer: "+err);
			res.status(500).send(err.message);
			return
		}else

		res.send( html.replace("data-server-rendered=\"true\"", ""));
	});
		}
  		catch (e) {
        console.log("entering catch block");
        console.log(e);
        console.log("leaving catch block");
      }

});

server.listen(4000, () => {
	console.log("@@ NodeServer: Server started on port 4000");
});
