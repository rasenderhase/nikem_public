/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 13.02.13
 * Time: 21:18
 *
 * Verschiedene Stadien eines Spiels:
 * - Angelegt: Es können Spieler hinzugefügt werden
 * * Übergang: Maximalanzahl an Spielern erreicht oder Spiel wird vom ersten Spieler gestartet
 *             und Minimalanzahl an Spielern vorhanden
 * - Gestartet: Es wird gemischt und gegeben, i = 0, s = 0, n = Anzahl der Spieler
 * * Übergang: -
 * - ZugSpieler[i % n]: Spieler[i % n] wählt die zu spielenden Karten abhängig vom Tisch, s++
 * * Übergang: Spieler[i % n] bestätigt die zu spielenden Karten oder Spieler[i % n] passt (d.h. nimmt die Karten).
 * - Spieler[i % n] füllt seine Karten auf (automatisch)
 * * Übergang
 * - hier gibt es mehrere Möglichkeiten:
 *      1. Der nächste Spieler ist an der Reihe: (s < n) oder Spieler[i % n] hat gepasst. i++
 *      3. Der Stich ist zu Ende und der nächste Spieler ist an der Reihe: Tisch wird geleert; s = n; i++;
 *      2. Das Spiel ist zu Ende: Spieler[i % n] hat keine Karten mehr und der Stapel ist leer.
 */


var SpielIljig, k, u;
u = require("../Util.js").Util;
k = require("./KartenspielIljig.js");

SpielIljig = function (id, maxanzahlSpieler) {
    var id = id || u.uuid();
    this.id = id;
    this.teilnahmeGeheimnis = u.uuid();
    this.status = this.STATUS.angelegt;
    this.trumpf = null;
    this.spielerNummerAnDerReihe = null;
    this.anzahlSpieler = 0;
    this.maxanzahlSpieler = maxanzahlSpieler;

    this.stapel = new k.StapelIljig();
    this.tisch = null;      //TODO Tisch programmieren
    this.spieler = [];
};

SpielIljig.prototype = Object.create(Object.prototype, {
    STATUS : {
        value : {
            angelegt : "Angelegt",
            gestartet : "Gestartet",
            zug : "Zug",
            ende : "Ende"
        }
    },
    addSpieler : {
        value : function(/* Spieler */ spieler) {
            if (this.status !== this.STATUS.angelegt) {
                throw {
                    name : "SpielLaeuftSchon",
                    message : "Es können keine weiteren Spieler mehr teilnehmen."
                }
            }

            if (this.spieler.length < this.maxanzahlSpieler) {

            } else {
                throw {
                    name : "ZuVieleSpieler",
                    message : "Es sind höchstens " + this.maxanzahlSpieler + " Spieler erlaubt."
                }
            }
            this.spieler.push(spieler);
            this.anzahlSpieler++;
        }
    },
    toJSON : {
        value : function() {
            return {
                id : this.id,
                status : this.status,
                trumpf : this.trumpf,
                spielerNummerAnDerReihe : this.spielerNummerAnDerReihe,
                anzahlSpieler : this.anzahlSpieler,
                maxanzahlSpieler : this.maxanzahlSpieler
            }
        }
    }
});

exports.SpielIljig = SpielIljig;