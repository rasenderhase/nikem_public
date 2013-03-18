/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 02.03.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

var dbService = require("../iljig/DBService.js").dbService
    handle = dbService.handle;

exports.load = function(req, res, next){
    var spielerId = req.param("spieler_id"),
        spielId = req.param("spiel_id"),
        spielCallback;

    spielCallback = function(spiel) {
        dbService.getSpieler(spielerId, handle(function (spieler) {
            req.atts = {
                spiel : spiel,
                spieler : spieler
            };
            next();
        }));
    };

    dbService.getSpiel(spielId, handle(spielCallback));
};

exports.save = function(req, res, next){
    var id = req.param("spieler_id"),
        adminGeheimnis = req.param("adminGeheimnis"),
        teilnahmeGeheimnis = req.param("teilnahmeGeheimnis"),
        spielerName = req.param("spielerName"),
        spieler = req.atts.spieler,
        spiel = req.atts.spiel,
        callback;

    callback = function() {
        res.header("location", req.path);
        next();
    };

    if (teilnahmeGeheimnis !== spiel.teilnahmeGeheimnis
        && adminGeheimnis !== spiel.adminGeheimnis) {
        //Wenn weder teilnahmeGeheimnis noch adminGeheimnis stimmen,
        //muss es sich um einen Hacker handlen
        next("Hacker!");
    } else if (!spieler) {
        spieler = new k.Spieler(id, spielerName, spiel.id);
        spiel.addSpieler(spieler);                              //TODO ???
        req.atts.spieler = spieler;
        res.status(201);
        dbService.saveSpieler(spieler, handle(callback));
    } else callback(null);
};

exports.view = function(req, res){
    var spiel = req.atts.spiel,
        spieler = req.atts.spieler,
        renderOptions = {};

    renderOptions.spiel = spiel;
    renderOptions.spieler = spieler;
    renderOptions.layout = "spielSpieler";

    res.render("spielSpieler", renderOptions);
};