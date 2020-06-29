package de.andrews.digsitevisualization.calculation;

public class MeasurementGroupIdentifier {

    private String unit;
    private Long id;

    public MeasurementGroupIdentifier(String unit, Long id) {
        this.unit = unit;
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MeasurementGroupIdentifier)) {
            return false;
        }

        MeasurementGroupIdentifier s = (MeasurementGroupIdentifier) o;

        if (unit.equals(((MeasurementGroupIdentifier) o).unit) && id.equals(((MeasurementGroupIdentifier) o).id)) {
            return true;
        } else {
            return false;
        }
    }
}
