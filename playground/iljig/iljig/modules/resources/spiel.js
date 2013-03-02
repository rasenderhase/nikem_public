/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 02.03.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

var dbService = require("../iljig/DBService.js").dbService,
    s = require("../iljig/SpielIljig.js"),
    u = require("../Util.js").Util;

exports.home = function(req, res) {

    //neues Spiel noch nicht persistieren...
    res.render("home", {
        spiel : new s.SpielIljig()
    });
};

exports.load = function(req, res, next){
    var id = req.param("spiel_id"),
        spiel;

    spiel = dbService.getSpiel(id);
    req.spiel = spiel;
    next();
};

exports.create = function(req, res, next){
    var id = req.param("spiel_id"),
        maxanzahlSpieler = parseInt(req.param("spiel_anzahlSpieler"), 10),
        spiel,
        statusCode = 200;

    spiel = req.spiel;
    if (!spiel) {
        spiel = new s.SpielIljig(id, maxanzahlSpieler);
        dbService.saveSpiel(spiel);
        res.status(201);
        req.spiel = spiel;
    }

    req.teilnahmeUrl = req.protocol + "://" + req.get('host') + req.url + "?teilnahme=" + spiel.teilnahmeGeheimnis;

    res.header("location", spiel.url);
    next();
};

exports.view = function(req, res){
    var spiel = req.spiel;
    res.render("spiel", {
        spiel : spiel,
        teilnahmeGeheimnis : req.teilnahmeUrl
    });
};

exports.list = function(req, res) {
    var spielList = dbService.getSpielList();

    res.format({
        html : function() {
            res.render("spiellist", {
                spielList : spielList
            });
        },
        json : function() {
            res.json(JSON.stringify(spielList));
        }
    });


};