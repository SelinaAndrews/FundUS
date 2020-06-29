package de.andrews.digsitevisualization.data;

import java.util.List;

public class GroupFinding {

    private Vertex vertex;
    private int layer;
    private List<SubGroupFinding> composition;

    public GroupFinding() {}

    public GroupFinding(Vertex vertex, int layer, List<SubGroupFinding> composition) {
        this.vertex = vertex;
        this.layer = layer;
        this.composition = composition;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public List<SubGroupFinding> getComposition() {
        return composition;
    }

    public void setComposition(List<SubGroupFinding> composition) {
        this.composition = composition;
    }
}
