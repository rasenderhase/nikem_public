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
k = require("./../Kartenspiel.js");
u = require("./../Util.js");

SpielIljig = function (id) {
    this.id = id || u.Util.uuid();
    this.status = this.STATUS.angelegt;
    this.trumpf;

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
            if (this.spieler.length < k.GeberIljig.SPIELER_ANZAHL_KARTEN.maxAnzahl) {

            } else {
                throw {
                    name : "ZuVieleSpieler",
                    message : "Es sind höchstens " + k.GeberIljig.SPIELER_ANZAHL_KARTEN.maxAnzahl + " Spieler erlaubt."
                }
            }
            this.spieler.push(spieler);
        }
    }
});

exports.SpielIljig = SpielIljig;