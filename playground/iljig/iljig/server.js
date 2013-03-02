/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 02.03.13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */


var express = require("express");
var app = express();

app.use(express.favicon(__dirname + "/public/images/icon.png"));
app.use(express.static(__dirname + "/public"));

app.get("/hello.txt", function(req, res){
    res.send("Hello World");
});

app.listen(3000);
console.log("Listening on port 3000");