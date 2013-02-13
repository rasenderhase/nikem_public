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

var DBService, s;
s = require("./SpielIljig.js");

DBService = function() {
    this.db = {
        spiel : {},
        spieler : {},
        spielerKarten : {},
        stapelKarten : {},
        spielerStichKarten : {},
        spielerStich : {}
    };
};

DBService.prototype = Object.create(Object.prototype, {
    saveSpiel : {
        value : function(/* SpielIljig */ spiel) {
            this.db.spiel[spiel.id] = {
                id : spiel.id,
                status : spiel.status,
                trumpf : spiel.trumpf,
                spielerNummerAnDerReihe : spiel.spielerNummerAnDerReihe,
                anzahlSpieler : spiel.anzahlSpieler
            }
        }
    },
    getSpiel : {
        value : function(/* String */ id) {
            var spiel;
            spiel = Object.create(s.SpielIljig.prototype);  //Instanziierung ohne Konstruktor
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