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
        value : function(/* SpielIljig */ spiel, /* function */ callback) {
            var err = null;
            try {
                spiel.lastAccess = Date.now();
                this.db.spiel[spiel.id] = spiel.toDb();
            } catch (e) {err = e;}
            if (callback) callback(err);
        }
    },
    getSpiel : {
        value : function(/* String */ id, /* function */ callback) {
            var spiel,
                err = null,
                result = null;
            try {
                this.deleteAlteSpiele(s.SpielIljig.MAX_SPIEL_ALTER);
                if (this.db.spiel[id]) {
                    spiel = new s.SpielIljig();
                    this.db.spiel[id].lastAccess = Date.now();
                    spiel.extend(this.db.spiel[id]);                //Persistierte Daten überbraten
                    result = spiel;
                }
            } catch (e) {err = e;}
            if (callback) callback(err, result);
            return result;
        }
    },
    getSpielList : {
        value : function(/* function */ callback) {
            var i, list = [],
                spiel,
                err = null,
                result = null;

            try {
                this.deleteAlteSpiele(s.SpielIljig.MAX_SPIEL_ALTER);
                for (i in this.db.spiel) {
                    if (this.db.spiel.hasOwnProperty(i)) {
                        spiel = new s.SpielIljig();
                        spiel.extend(this.db.spiel[i]);
                        list.push(spiel);
                    }
                }
                result = list;
            } catch (e) {err = e;}
            if (callback) callback(err, result);
            return result;
        }
    },
    deleteAlteSpiele : {
        value : function(/* Date */ maxAlterMs, /* function */ callback) {
            var err = null, i = null, spiel,
                minDate = Date.now() - maxAlterMs;
            try {
                for (i in this.db.spiel) {
                    if (this.db.spiel.hasOwnProperty(i)) {
                        if (this.db.spiel[i].lastAccess < minDate) {
                            delete this.db.spiel[i];
                        }
                    }
                }
            } catch (e) {err = e;}
            if (callback) callback(err);
        }
    },
    /**
     * Default handler for function (err, result) type callbacks.
     */
    handle : {
        value : function (/* function */ success, /* function */ fail) {
            return function (err, result) {
                if (err) {
                    if (fail) fail(err);
                    else throw err;
                } else {
                    success(result);
                }
            }
        }
    }
});

exports.dbService = new DbService();