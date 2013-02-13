/**
 * Created with JetBrains WebStorm.
 * User: andreas
 * Date: 12.01.13
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 *
 * see http://www.bennadel.com/blog/2184-Object-create-Improves-Constructor-Based-Inheritance-In-Javascript-It-Doesn-t-Replace-It.htm
 */

var k = require("./modules/iljig/KartenspielIljig.js"),
    s = require("./modules/iljig/SpielIljig.js"),
    d = require("./modules/iljig/DBService.js");

var stapel = new k.StapelIljig();

var andi = new k.Spieler("andi");
var martin = new k.Spieler("martin");

var spiel = new s.SpielIljig();
var dbService = new d.DbService();

new k.GeberIljig().gib(stapel, [ andi, martin ]);


var handSorter = new k.HandSorterIljig(stapel.getTrumpf());
andi.handSorter = handSorter;
martin.handSorter = handSorter;

andi.sortHand();
martin.sortHand();

console.log("" + stapel);
console.log("" + andi);
console.log("" + martin);

console.dir(spiel);
dbService.saveSpiel(spiel);
spiel = dbService.getSpiel(spiel.id);

console.dir(spiel);