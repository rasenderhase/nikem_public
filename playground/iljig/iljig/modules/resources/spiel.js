/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 02.03.13
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

var dbService = require("../iljig/DBService.js").dbService,
    s = require("../iljig/SpielIljig.js"),
    u = require("../Util.js").Util,
    handle = dbService.handle;

exports.home = function(req, res) {
    var spielId = u.uuid();
    res.render("home", {
        spiel : {
            id : spielId
        }
    });
};

exports.load = function(req, res, next){
    var id = req.param("spiel_id");

    dbService.getSpiel(id, handle(function (spiel) {
        req.atts = {
            spiel : spiel
        };
        next();
    }));
};

exports.save = function(req, res, next){
    var id = req.param("spiel_id"),
        spiel = req.atts.spiel,
        callback;

    callback = function() {
        req.atts = {
            spiel : spiel,
            teilnahmeUrl :req.protocol + "://" + req.get('host') + req.url + "?teilnahme=" + spiel.teilnahmeGeheimnis,
            adminGeheimnis : spiel.adminGeheimnis
        };
        res.header("location", spiel.url);
        next();
    };

    if (!spiel) {
        spiel = new s.SpielIljig(id);
        res.status(201);
        dbService.saveSpiel(spiel, handle(callback));
    } else {
        callback(null);
    }
};

exports.view = function(req, res){
    var spiel = req.atts.spiel;
    res.render("spiel", {
        spiel : spiel,
        teilnahmeUrl : req.atts.teilnahmeUrl,
        adminGeheimnis : req.atts.adminGeheimnis
    });
};

exports.list = function(req, res) {
    res.format({
        html : function() {
            dbService.getSpielList(handle(function (spielList) {
                res.render("spiellist", {
                    spielList : spielList
                });
            }));
        },
        json : function() {
            dbService.getSpielList(handle(function (spielList) {
                var i, json = [];
                for (i = 0; i < spielList.length; i++) {
                    json[i] = {
                        id : spielList[i].id,
                        status : spielList[i].status,
                        trumpf : spielList[i].trumpf,
                        spielerNummerAnDerReihe : spielList[i].spielerNummerAnDerReihe
                    }
                }
                res.json(json);
            }));
        }
    });
};