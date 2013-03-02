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
    res.render("home", {
        spiel : {
            id :u.uuid
        }
    });
};

exports.load = function(req, res, next){
    var id = req.param("spiel_id");

    dbService.getSpiel(id, u.handle(function (spiel) {
        req.spiel = spiel;
        next();
    }));
};

exports.create = function(req, res, next){
    var id = req.param("spiel_id"),
        spiel = req.spiel,
        callback;

    callback = function() {
        req.spiel = spiel;
        req.teilnahmeUrl = req.protocol + "://" + req.get('host') + req.url + "?teilnahme=" + spiel.teilnahmeGeheimnis;

        res.status(201);
        res.header("location", spiel.url);
        next();
    };

    if (!spiel) {
        spiel = new s.SpielIljig(id);
        dbService.saveSpiel(spiel, u.handle(callback));
    } else {
        callback(null);
    }
};

exports.view = function(req, res){
    var spiel = req.spiel;
    res.render("spiel", {
        spiel : spiel,
        teilnahmeGeheimnis : req.teilnahmeUrl
    });
};

exports.list = function(req, res) {
    res.format({
        html : function() {
            dbService.getSpielList(u.handle(function (spielList) {
                res.render("spiellist", {
                    spielList : spielList
                });
            }));
        },
        json : function() {
            dbService.getSpielList(u.handle(function (spielList) {
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