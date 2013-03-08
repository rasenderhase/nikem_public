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
        spielId = req.param("spiel_id");

    res.status(201);
    res.render("spielSpieler", {
        spieler : {
            id : spielerId,
            name : req.param("spielerName")
        },
        layout : "spielSpieler"
    });
};