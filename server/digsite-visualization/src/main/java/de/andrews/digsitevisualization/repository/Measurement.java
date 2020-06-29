package de.andrews.digsitevisualization.repository;

public class Measurement {

    private String UNIT;        //Quadratnummer im lokalen Messnetz
    private Long ID;            //Fortlaufende Fundnummer pro Grabungsquadrat
    private Long SUFFIX;        //Unterfundnummer z.B. bei Sammelfunden oder topographischen Messeinheiten mit mehreren Punkten
    private String X;           //X-Koordinate (Ost)
    private String Y;           //Y-Koordinate (Nord)
    private String Z;           //Z-Koordinate (Höhe im lokalen Messnetz, wird nach unten kleiner)
    private String GH;          //Geologischer Horizont (hier bei Übertragung z.T. Fehler)
    private String AH;          //Archäologischer Horizont
    private String BEST;        //Datenbestimmung (handelt es sich um Topographische Messdaten, Silex etc.)
    private String GF;          //Grundform (Bestimmung der vorhandenen Grundform)
    private String DEF;         //Definition (spezifische Kurzangabe zur näheren Bestimmung des jeweiligen Objektes)
    private String BEMERK;      //Bemerkung (freies Feld)
    private Boolean BEARBEITET; //trägt das Objekt sichtbare Bearbeitungsspuren
    private Boolean EF;         //handelt es sich um einen Einzelfund?

    public Measurement(String UNIT, Long ID, Long SUFFIX, String x, String y, String z, String GH, String AH, String BEST, String GF, String DEF, String BEMERK, Boolean BEARBEITET, Boolean EF) {
        this.UNIT = UNIT;
        this.ID = ID;
        this.SUFFIX = SUFFIX;
        X = x;
        Y = y;
        Z = z;
        this.GH = GH;
        this.AH = AH;
        this.BEST = BEST;
        this.GF = GF;
        this.DEF = DEF;
        this.BEMERK = BEMERK;
        this.BEARBEITET = BEARBEITET;
        this.EF = EF;
    }

    public String getUNIT() {
        return UNIT;
    }

    public Long getID() {
        return ID;
    }

    public Long getSUFFIX() {
        return SUFFIX;
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
