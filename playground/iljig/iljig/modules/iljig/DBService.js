/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 13.02.13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 *
 * Datenmodell:
 *
 * spiel -- 1:n -- spieler
 * spieler -- 1:n -- spielerkarten     (Karten auf der Hand eines Spielers)
 * spiel -- 1:n -- stapelkarten        (Karten im Stapel)
 * spiel -- 1:n -- spielerStich
 * spielerStich -- 1:n -- spielerStichkarten (von einem Spieler in einem Stich gespielte Karten)
 */

var DbService, s;
s = require("./SpielIljig.js");

DbService = function() {
    this.db = {
        spiel : {},
        spieler : {},
        spielerKarten : {},
        stapelKarten : {},
        spielerStichKarten : {},
        spielerStich : {}
    };
};

DbService.prototype = Object.create(Object.prototype, {
    saveSpiel : {
        value : function(/* SpielIljig */ spiel) {
            this.db.spiel[spiel.id] = spiel.toJSON();
        }
    },
    getSpiel : {
        value : function(/* String */ id) {
            var spiel;
            spiel = new s.SpielIljig();
            spiel.extend(this.db.spiel[id]);                //Persistierte Daten überbraten
            return spiel;
        }
    },
    deleteSpiel : {
        value : function(/* String */ id) {
            delete this.db.spiel[id];
        }
    }
});

exports.DbService = DbService;