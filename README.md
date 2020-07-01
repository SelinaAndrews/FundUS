# FundUS
FundUS - Das Fundstellen-Untersuchungssystem für paläolithische Grabungsdaten
_Betaversion 0.3.1_

## Was ist FundUS?

FundUS (Fundstellen-Untersuchungssystem) in eine interaktive Anwendung zur 3D-Visualisierung von paläolithischen Grabungsdaten in schematischer Darstellung. Das Ziel der Software ist es, eine paläolithische Fundstelle am Computer zu rekonstruieren, um daran archäologische Auswertungen vornehmen zu können.

Erarbeitet wurde die Software in Kooperation mit der Arbeitsgruppe Floss von der Abteilung Ältere Urgeschichte und Quartärökologie des Instituts für Ur- und Frühgeschichte und Archäologie des Mittelalters der Eberhard Karls Universität Tübingen.

Die Anwendung basiert auf eine Java Spring-Server und einer in Unreal Engine 4.24 entwickelten Visualisierungsanwendung.

### Für wen wurde FundUS entwickelt?

Die Zielgruppe sind paläolithische Archäolog\*innen, die mit den während einer Grabung aufgenommenen Daten arbeiten und diese visualisieren wollen.

### Was kann FundUS?

In der aktuellen Betaversion 0.3.1 verfügt die Anwendung über folgende Funktionen:

- Über einzelne Punkte eingemessene Oberflächen aus dem Verlauf der Grabung rekonstruieren und nach geologischen Horizonten gruppiert anzeigen
- Einzeln dreidimensional eingemessene Einzelfunde anzeigen, wobei unterschiedliche Fundkategorien durch verschiedene Symbole differenziert werden
- Gemeinsam eingemessene Sammelfunde als ein Fundsymbol anzeigen und im seitlichen Infokasten die einzelnen Sammelfundbestandteile auflisten
- Infokasten für Einzelfunde und Sammelfunde anzeigen
- Koordinatensystem mit Unterteilung der Quadratmeter der Fundstelle darstellen
- Wechsel zwischen verschiedenen Kameraeinstellungen: orthografisch und perspektivisch, fixierte Kamera oder freie Kamera mit WASD-Steuerung, voreingestellte Kameraansichten wie Draufsicht oder Seitenansicht
- Filtern der dargestellten Objekte nach geologischen Horizonten und Fundkategorien

### Was wird für die Verwendung von FundUS benötigt?

- Access-Datenbank mit den eingemessenen Funden und Oberflächen einer Fundstelle (siehe Abschnitt Datenformat)
- Installierte Java-Version
- Direct X Runtime 10+
- Windows-Betriebssystem
- freier Port 2020

### Datenformat

Das von FundUS erwartete Datenformat der Microsoft Access-Datenbank entspricht dem Schema der Arbeitsgruppe Floss. Zu Testzwecken wird in Kürze eine Testdatenbank bereitgestellt (befindet sich zur Zeit in Arbeit).

## Zukünftige Entwicklung

- Beispieldatenbank erstellen
- Unreal-Visualisierung von Blueprint zu C++ konvertieren