package de.andrews.digsitevisualization.data;

public class Vertex {

    private int index;
    private double x;
    private double y;
    private double z;

    public Vertex() {};

    public Vertex(double x, double y, double z) {
        this.index = -1;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(int index, double x, double y, double z) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
