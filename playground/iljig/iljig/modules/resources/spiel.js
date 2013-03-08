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
    url = require('url'),
    handle = dbService.handle;

exports.home = function(req, res) {
    var spielId = u.uuid(),
        adminGeheimnis = u.uuid();
    res.cookie("adminGeheimnis", adminGeheimnis, {
        path: "/spiel/" + spielId,
//      maxAge : -1,         //Browser session cookie
        httpOnly: true      //Cookie darf vom Client nicht angefasst werden.
    });
    res.render("home", {
        spiel : {
            id : spielId,
            adminGeheimnis : adminGeheimnis
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
        adminGeheimnis = req.param("adminGeheimnis"),
        spiel = req.atts.spiel,
        callback;

    callback = function() {
        res.header("location", spiel.url);
        next();
    };

    if (!spiel) {
        spiel = new s.SpielIljig(id, adminGeheimnis);
        req.atts.spiel = spiel;
        res.status(201);
        dbService.saveSpiel(spiel, handle(callback));
    } else if (adminGeheimnis === spiel.adminGeheimnis) {
        callback(null);
    } else next("Hacker!");
};

exports.view = function(req, res){
    var spiel = req.atts.spiel,
        teilnahmeGeheimnis = req.param("teilnahmeGeheimnis"),
        adminGeheimnis = req.param("adminGeheimnis") || req.cookies.adminGeheimnis,
        renderOptions = {}, baseUrl;

    renderOptions.spiel = spiel;
    renderOptions.teilnahmeUrl = req.atts.teilnahmeUrl;
    renderOptions.adminGeheimnis = req.atts.adminGeheimnis;
    if (spiel.teilnahmeGeheimnis === teilnahmeGeheimnis) {
        renderOptions.teilnehmer = true;
        renderOptions.teilnahmeGeheimnis = teilnahmeGeheimnis;
        renderOptions.spieler = { id : u.uuid() };
    }
    if (spiel.adminGeheimnis === adminGeheimnis) {
        renderOptions.teilnehmer = true;    //Ein Admin darf auch teilnehmen...
        renderOptions.teilnahmeGeheimnis = spiel.teilnahmeGeheimnis;
        renderOptions.spieler = { id : u.uuid() };
            baseUrl = url.parse(req.url);
        renderOptions.admin = true;
        renderOptions.teilnahmeUrl = req.protocol + "://" + req.get("host") + baseUrl.pathname + "?teilnahmeGeheimnis=" + spiel.teilnahmeGeheimnis;
        renderOptions.adminUrl = "?adminGeheimnis=" + adminGeheimnis;
    }

    res.render("spiel-" + spiel.status, renderOptions);
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