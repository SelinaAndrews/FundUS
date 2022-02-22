package de.andrews.digsitevisualization.visualization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.andrews.digsitevisualization.data.GroupFinding;
import de.andrews.digsitevisualization.data.SingularFinding;
import de.andrews.digsitevisualization.data.Surface;

import java.util.HashMap;
import java.util.List;

public class Visualization {

    private String info;
    private List<Surface> surfaces;
    private List<SingularFinding> singles;
    private List<GroupFinding> buckets;
    private HashMap<String, String> metaData;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<Surface> getSurfaces() {
        return surfaces;
    }

    public void setSurfaces(List<Surface> surfaces) {
        this.surfaces = surfaces;
    }

    public List<SingularFinding> getSingles() {
        return singles;
    }

    public void setSingles(List<SingularFinding> singles) {
        this.singles = singles;
    }

    public List<GroupFinding> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<GroupFinding> buckets) {
        this.buckets = buckets;
    }

    public HashMap<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(HashMap<String, String> metaData) {
        this.metaData = metaData;
    }

    public String generateJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
