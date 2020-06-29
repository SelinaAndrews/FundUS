package de.andrews.digsitevisualization.data;

import java.util.List;

public class Profile {

    private List<Vertex> vertices;
    private int layer;

    public Profile() {
    }

    public Profile(List<Vertex> vertices, int layer) {
        this.vertices = vertices;
        this.layer = layer;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
