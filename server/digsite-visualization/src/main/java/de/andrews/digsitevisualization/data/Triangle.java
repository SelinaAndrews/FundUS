package de.andrews.digsitevisualization.data;

public class Triangle {

    private int point_a;
    private int point_b;
    private int point_c;

    public Triangle() {}

    public Triangle(int point_a, int point_b, int point_c) {
        this.point_a = point_a;
        this.point_b = point_b;
        this.point_c = point_c;
    }

    public int getPoint_a() {
        return point_a;
    }

    public void setPoint_a(int point_a) {
        this.point_a = point_a;
    }

    public int getPoint_b() {
        return point_b;
    }

    public void setPoint_b(int point_b) {
        this.point_b = point_b;
    }

    public int getPoint_c() {
        return point_c;
    }

    public void setPoint_c(int point_c) {
        this.point_c = point_c;
    }
}
