package de.andrews.digsitevisualization.data;

import java.util.List;

public class Surface {

    private List<Vertex> vertices;
    private List<Triangle> connections;
    private int layer;
    private String id;

    public Surface() {}

    public Surface(List<Vertex> vertices, List<Triangle> connections, int layer, String label, String id) {
        this.vertices = vertices;
        this.connections = connections;
        this.layer = layer;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
