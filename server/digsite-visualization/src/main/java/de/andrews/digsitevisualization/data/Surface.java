package de.andrews.digsitevisualization.data;

import java.util.List;

public class Surface {

    private List<Vertex> vertices;
    private List<Triangle> connections;
    private int layer;

    public Surface() {}

    public Surface(List<Vertex> vertices, List<Triangle> connections, int layer, String label) {
        this.vertices = vertices;
        this.connections = connections;
        this.layer = layer;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Triangle> getConnections() {
        return connections;
    }

    public void setConnections(List<Triangle> connections) {
        this.connections = connections;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
