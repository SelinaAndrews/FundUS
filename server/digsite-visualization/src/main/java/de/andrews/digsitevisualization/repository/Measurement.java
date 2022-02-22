package de.andrews.digsitevisualization.repository;

import java.util.*;
import java.util.stream.Collectors;

public class Measurement {

    private String findingNumber = null;

    private final String UNIT;        //Quadratnummer im lokalen Messnetz
    private final Long ID;            //Fortlaufende Fundnummer pro Grabungsquadrat
    private final String X;           //X-Koordinate (Ost)
    private final String Y;           //Y-Koordinate (Nord)
    private final String Z;           //Z-Koordinate (Höhe im lokalen Messnetz, wird nach unten kleiner)
    private final String GH;          //Geologischer Horizont (hier bei Übertragung z.T. Fehler)
    private final String AH;          //Archäologischer Horizont
    private final String BEST;        //Datenbestimmung (handelt es sich um Topographische Messdaten, Silex etc.)
    private final String GF;          //Grundform (Bestimmung der vorhandenen Grundform)
    private final String DEF;         //Definition (spezifische Kurzangabe zur näheren Bestimmung des jeweiligen Objektes)
    private final String BEMERK;      //Bemerkung (freies Feld)
    private final Boolean BEARBEITET; //trägt das Objekt sichtbare Bearbeitungsspuren

    private Boolean EF;               //handelt es sich um einen Einzelfund? wird später auf false gesetzt falls wir eine Gruppe identifizieren konnten
    private String EFColumn;

    private Boolean TOPO;             //handelt es sich um eine Fläche?
    private final List<String> bestId;

    private final List<String> findingNumberParts;
    private final List<String> groupFindingNumberParts;

    private Map<String,String> remainingData;

    private Type serverInternalType;

    public Measurement(HashMap<String,String> date, MeasurementMapping mapping) {

        List<String> booleanKeys = Arrays.asList("true", "1", "TRUE", "True", "True()", "true()", "TRUE()");
        /*
        1. koordinaten über x y z definieren
        2. id über mehrere felder definieren (unit id suffix)
        3. gh, ah, gf, def, bemerk, bearbeitet wie gehabt, können null sein
        4. einzelfunde werden anhand der koordinaten auf true oder false gesetzt
        5. best(topo) in zwei variablen umändern, topo boolean und topo id (evtl auch objekt?)
        6. restliche sachen speichern
         */

        //1.
        this.X = date.remove(mapping.getX());
        this.Y = date.remove(mapping.getY());
        this.Z = date.remove(mapping.getZ());
        //2.
        this.findingNumberParts = new ArrayList<>();
        if (mapping.getFindingNumberParts() != null) {
            this.findingNumberParts.addAll(
                    mapping.getFindingNumberParts().stream()
                            .map(date::get)
                            .collect(Collectors.toList())
            );
        }
        this.groupFindingNumberParts = new ArrayList<>();
        if (mapping.getGroupFindingNumberParts() != null) {
            this.groupFindingNumberParts.addAll(
                    mapping.getGroupFindingNumberParts().stream()
                            .map(date::get)
                            .collect(Collectors.toList())
            );
        }
        //3.
        this.GH = date.remove(mapping.getGH());
        this.AH = date.remove(mapping.getAH());
        this.GF = date.remove(mapping.getGF());
        this.DEF = date.remove(mapping.getDEF());
        this.BEMERK = date.remove(mapping.getBEMERK());
        this.BEARBEITET = Boolean.parseBoolean(date.remove(mapping.getBEARBEITET()));
        //4.
        this.EF = true;
        this.EFColumn = date.remove(mapping.getEFColumn());
        List<String> efKeys = new ArrayList<>();
        if (mapping.getEFKeys() != null) {
            efKeys.addAll(mapping.getEFKeys());
        }
        //"GH = 3 or 2"
        if(mapping.isEfBool()) {
            efKeys.addAll(booleanKeys);
        }
        if (this.EFColumn != null && !this.EFColumn.isEmpty()) {
            if (efKeys.stream().noneMatch(this.EFColumn::equals)) {
                this.EF = false;
            }
        }
        //5.
        this.BEST = date.remove(mapping.getBEST());
        this.bestId = new ArrayList<>();
        if (mapping.getBestId() != null) {
            this.bestId.addAll(
                mapping.getBestId().stream()
                    .map(date::get)
                    .collect(Collectors.toList())
            );//nötig zur gruppierung der topos und gf
        }
        this.TOPO = false;
        List<String> topoKeys = new ArrayList<>();
        if (mapping.getTopoKeys() != null) {
            topoKeys.addAll(mapping.getTopoKeys());
        }
        if (mapping.isTopoBool()) {
            topoKeys.addAll(booleanKeys);
        }
        if (this.BEST != null) {
            if (topoKeys.stream().anyMatch(this.BEST::equals)) {
                this.TOPO = true;
                this.EF = false;
            }
        }
        //6.
        this.remainingData = date;

        this.UNIT = null;
        this.ID = null;

    }

    public enum Type {
        SINGLEFIND,
        GROUPFIND,
        SURFACE
    }

    public Type getType() {
        return serverInternalType;
    }

    public void setType(Type serverInternalType) {
        this.serverInternalType = serverInternalType;
    }

    public String getFindingNumber() {
        return findingNumber;
    }

    public void setFindingNumber(String findingNumber) {
        this.findingNumber = findingNumber;
    }

    public Map<String, String> getRemainingData() {
        return remainingData;
    }

    public void setRemainingData(Map<String, String> remainingData) {
        this.remainingData = remainingData;
    }

    public List<String> getBestId() {
        return bestId;
    }

    public List<String> getFindingNumberParts() {
        return findingNumberParts;
    }

    public List<String> getGroupFindingNumberParts() {
        return groupFindingNumberParts;
    }

    public Boolean getTOPO() {
        return TOPO;
    }

    public String getUNIT() {
        return UNIT;
    }

    public Long getID() {
        return ID;
    }

    public String getX() {
        return X;
    }

    public String getY() {
        return Y;
    }

    public String getZ() {
        return Z;
    }

    public String getGH() {
        return GH;
    }

    public String getAH() {
        return AH;
    }

    public String getBEST() {
        return BEST;
    }

    public String getGF() {
        return GF;
    }

    public String getDEF() {
        return DEF;
    }

    public String getBEMERK() {
        return BEMERK;
    }

    public Boolean getBEARBEITET() { return BEARBEITET; }

    public Boolean getEF() {
        return EF;
    }
}