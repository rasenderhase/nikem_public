Kartenspiel
===

### Rollen:
       * Admin
       * Teilnehmer
       * Zuschauer

### Ablauf der Spiel-Anlage:

       * Spiel anlegen (Admin)              post /spiel/:spiel_id
       * Teilnehmer einladen (Admin)        email
       * teilnehmen  (Admin, Teilnehmer)    post /spiel/:spiel_id/spieler/:spieler_id
       * Spiel starten (Admin)              post /spiel/:spiel_id status="Gestartet"
       * Spielstatus aktualisieren (alle)   get  /spiel/:spiel_id