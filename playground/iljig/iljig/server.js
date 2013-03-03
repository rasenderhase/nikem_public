/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 02.03.13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */


var express = require("express"),
    exphbs  = require("express3-handlebars"),
    i18n = require("i18next"),
    u = require("./modules/Util.js"),
    spiel = require("./modules/resources/spiel.js"),

    app = express();

i18n.init({supportedLngs: ['en', 'de']});

app.engine("handlebars", exphbs({
    defaultLayout: "main",
    helpers : {
        t : i18n.t
    }
}));
app.set("view engine", "handlebars");

app.use(express.logger('dev'));
app.use(i18n.handle);
app.use(express.bodyParser());
app.use(express.favicon(__dirname + "/public/images/icon.png"));
app.use(express.static(__dirname + "/public"));

i18n.registerAppHelper(app);    //Register AppHelper so you can use the translate function inside template

app.get("/", spiel.home);

app.get("/spiel", spiel.list);

app.all("/spiel/:spiel_id", spiel.load);
app.post("/spiel/:spiel_id", spiel.save);
app.all("/spiel/:spiel_id", spiel.view);

app.listen(3000);
console.log("Listening on port 3000");