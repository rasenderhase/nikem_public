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
s = require("./SpielIljig.js"),
k = require("./KartenspielIljig.js");

DbService = function() {
    this.db = {
        spiel : {},
        spieler : {},
        spielerKarten : {},
        stapelKarten : {},
        spielerStichKarten : {},
        spielerStich : {},
        index : {
            spielSpieler : {}
        }
    };
};

DbService.prototype = Object.create(Object.prototype, {
    saveSpiel : {
        value : function(/* SpielIljig */ spiel, /* function */ callback) {
            var err = null;
            try {
                spiel.lastAccess = Date.now();
                this.db.spiel[spiel.id] = spiel.toDb();
                this.db.index.spielSpieler[spiel.id] = {};
            } catch (e) {err = e;}
            if (callback) callback(err);
        }
    },
    getSpiel : {
        value : function(/* String */ id, /* function */ callback) {
            var spiel, spielerList, i = null,
                err = null,
                result = null;
            try {
                this.deleteAlteSpiele(s.SpielIljig.MAX_SPIEL_ALTER);
                if (this.db.spiel[id]) {
                    spiel = new s.SpielIljig();
                    this.db.spiel[id].lastAccess = Date.now();
                    spiel.extend(this.db.spiel[id]);                //Persistierte Daten überbraten

                    spielerList = this.getSpielerBySpiel(id);
                    for (i in spielerList) {
                        spiel.addSpieler(spielerList[i]);
                    }
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
                spielerList,
                err = null,
                result = null;

            try {
                this.deleteAlteSpiele(s.SpielIljig.MAX_SPIEL_ALTER);
                for (i in this.db.spiel) {
                    if (this.db.spiel.hasOwnProperty(i)) {
                        spiel = new s.SpielIljig();
                        spiel.extend(this.db.spiel[i]);

                        spielerList = this.getSpielerBySpiel(spiel.id);
                        for (i in spielerList) {
                            spiel.addSpieler(spielerList[i]);
                        }
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
            var err = null, i = null, j= null, spiel, index,
                minDate = Date.now() - maxAlterMs;
            try {
                for (i in this.db.spiel) {
                    if (this.db.spiel.hasOwnProperty(i)) {
                        if (this.db.spiel[i].lastAccess < minDate) {
                            spiel = this.db.spiel[i];
                            index = this.db.index.spielSpieler[spiel.id];
                            for (j in index) {
                                if (index.hasOwnProperty(j)) {
                                    delete this.db.spieler[j];
                                }
                            }

                            delete spiel;
                        }
                    }
                }
            } catch (e) {err = e;}
            if (callback) callback(err);
        }
    },

    saveSpieler : {
        value : function(/* k.Spieler */ spieler, /* function */ callback) {
            var err = null;
            try {
                this.db.spieler[spieler.id] = spieler.toDb();
                this.db.index.spielSpieler[spieler.spielId][spieler.id] = spieler.id;
            } catch (e) {err = e;}
            if (callback) callback(err);
        }
    },
    getSpieler : {
        value : function(/* String */ id, /* function */ callback) {
            var spieler,
                err = null,
                result = null;
            try {
                if (this.db.spieler[id]) {
                    spieler = new k.Spieler();
                    spieler.extend(this.db.spieler[id]);
                    result = spieler;
                }
            } catch (e) {err = e;}
            if (callback) callback(err, result);
            return result;
        }
    },
    getSpielerBySpiel : {
        value : function(/* String */ spielId, /* function */ callback) {
            var spielerList = [],
                spieler,
                spielIndex,
                spielerId,
                err = null,
                result = null;
            try {
                spielIndex = this.db.index.spielSpieler[spielId];
                for (spielerId in spielIndex) {
                    if (spielIndex.hasOwnProperty(spielerId)) {
                        spieler = new k.Spieler();
                        spieler.extend(this.db.spieler[spielerId]);
                        spielerList.push(spieler);
                        spielerList.sort(function (a, b) { return a.nummer - b.nummer});
                    }
                }
                result = spielerList;
            } catch (e) {err = e;}
            if (callback) callback(err, result);
            return result;
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