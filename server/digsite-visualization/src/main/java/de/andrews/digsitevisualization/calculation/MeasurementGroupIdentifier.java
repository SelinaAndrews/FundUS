package de.andrews.digsitevisualization.calculation;

import java.util.List;

public class MeasurementGroupIdentifier {

    private String unit;
    private Long id;
    private List<String> bestId;

    public MeasurementGroupIdentifier(String unit, Long id) {
        this.unit = unit;
        this.id = id;
    }

    public MeasurementGroupIdentifier(List<String> bestId) {
        this.bestId = bestId;
    }

    public List<String> getBestId() {
        return bestId;
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

        if (!(o instanceof MeasurementGroupIdentifier) || this.bestId==null || ((MeasurementGroupIdentifier) o).bestId==null ) {
            return false;
        }

        MeasurementGroupIdentifier s = (MeasurementGroupIdentifier) o;

        if (bestId.size() == ((MeasurementGroupIdentifier) o).bestId.size()) {
            for (int i = 0; i < bestId.size(); i++) {
                String lhs = bestId.get(i);
                String rhs = ((MeasurementGroupIdentifier) o).bestId.get(i);
                if (lhs==null || rhs==null || !lhs.equals(rhs)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
