_For English version, see below._

# FundUS - Das Fundstellen-Untersuchungssystem für paläolithische Grabungsdaten
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

- eine Microsoft Access-Datenbank mit den eingemessenen Funden und Oberflächen einer Fundstelle (siehe Abschnitt Datenformat)
- Java-Runtime
- Direct X Runtime 10+
- Windows-Betriebssystem (getestet wurde bisher ausschließlich Windows 10)
- freier Port 2020

### Datenformat

Das von FundUS erwartete Datenformat der Microsoft Access-Datenbank entspricht dem Schema der Arbeitsgruppe Floss. Zu Testzwecken wird in Kürze eine Testdatenbank bereitgestellt (befindet sich zur Zeit in Arbeit).

## Zukünftige Entwicklung

- Beispieldatenbank erstellen
- Unreal-Visualisierung von Blueprint zu C++ konvertieren

## Verwendete Softwarepakete und Unreal-Plugins

Java Spring Server
https://spring.io/

Delaunay Triangulator von Johannes Diemke
https://github.com/jdiemke/delaunay-triangulator

UCanAccess
http://ucanaccess.sourceforge.net/site.html

VaREST Plugin für Unreal Engine
https://github.com/ufna/VaRest

Extended Standard Library für Unreal Engine
https://code.lowentry.com/Applications/Plugins/UE4/ExtendedStandardLibrary

---

# [EN] FundUS - The digsite analysis tool for paleolithic archaeology
_Betaversion 0.3.1_

## What is FundUS?

FundUS is an interactive tool for displaying a schematic 3D view of paleolithic digsite data. It's goal is to reconstruct a paleolithic digsite on the computer for use in archaeological analysis.

This software was developed in cooperation with the workgroup Floss of the Department of Early Prehistory and Quaternary Ecology of the Institute of Prehistory, Early History and Medieval Archaeology at the Eberhard Karls Universität Tübingen.

The software is based on a Java Spring server and a complementary visualization software developed in Unreal Engine 4.24.

### Who was FundUS made for?

FundUS was developed for paleolithic archaeologists who want to work with the data collected at the digsite and want to visualize it in 3D.

### What are the features of FundUS?

In it's current state (Beta version 0.3.1), FundUS has the following features:

- reconstruct surfaces described by point measurements from the dig and group them by geological horizons
- display single findings at their measured location and differentiate the finding categories through the use of different symbols
- display group findings as a single symbol and list their composition in an info bar to the side
- show an info bar for further information on both single and group findings
- show a coordinate system with the square meter grid used at the digsite
- switch between different camera modes, including orthographic and perspective view, fixed or free camera, and preset camera locations like top-down view
- filter the objects in the scene by geological horizons and finding categories

### What is needed to run FundUS?

- a Microsoft Access database with the data from the digsite (see section Data format)
- Java Runtime installed
- Direct X Runtime 10 (or higher) installed
- a Windows operating system (currently only tested on Windows 10)
- port 2020 needs to be free

### Data format

FundUS expects it's Microsoft Access database to be formatted according to the schema of the workgroup Floss. A database for test usage is currently worked on and will be added soon.

## Planned development

- create a test database
- convert blueprints in Unreal visualization software to C++

## Software libraries and Unreal plugins used

Java Spring Server
https://spring.io/

Delaunay Triangulator by Johannes Diemke
https://github.com/jdiemke/delaunay-triangulator

UCanAccess
http://ucanaccess.sourceforge.net/site.html

VaREST Plugin for Unreal Engine
https://github.com/ufna/VaRest

Extended Standard Library for Unreal Engine
https://code.lowentry.com/Applications/Plugins/UE4/ExtendedStandardLibrary