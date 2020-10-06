package de.andrews.digsitevisualization.calculation;

import de.andrews.digsitevisualization.data.Triangle;
import de.andrews.digsitevisualization.data.Vertex;
import io.github.jdiemke.triangulation.DelaunayTriangulator;
import io.github.jdiemke.triangulation.NotEnoughPointsException;
import io.github.jdiemke.triangulation.Triangle2D;
import io.github.jdiemke.triangulation.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class SurfaceCalculator {

    /** Triangulate the delaunay representation of a given list of points and return a list of triangles. **/
    public List<Triangle> calculateSurface(List<Vertex> pointList, boolean isProfile) throws NotEnoughPointsException {
        List<Vector2D> pointSet;
        if (isProfile) {
            pointSet = prepareProfilePointSet(pointList);
        } else {
            pointSet = preparePointSet(pointList);
        }
        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);

        delaunayTriangulator.shuffle();
        delaunayTriangulator.triangulate();
        List<Triangle2D> triangleSoup = delaunayTriangulator.getTriangles();

        return formatConnections(triangleSoup);
    }

    /** Convert 3D vertices into 2D points by ignoring the z axis. **/
    private List<Vector2D> preparePointSet(List<Vertex> pointList) {
        List<Vector2D> pointSet = new ArrayList<>();

        for (Vertex p : pointList) {
            Vector2D point2D = new Vector2D(p.getIndex(), p.getX(), p.getY());
            pointSet.add(point2D);
        }

        return pointSet;
    }

    /** Convert 3D vertices into 2D points by checking if the x or the y axis has the higher variance and ignoring the axis with the lower variance. **/
    private List<Vector2D> prepareProfilePointSet(List<Vertex> pointList) {
        List<Vector2D> pointSet = new ArrayList<>();

        //Calculate variance for X and Y values
        double sumX = 0.0;
        double sumY = 0.0;

        for (Vertex p : pointList) {
            sumX += p.getX();
            sumY += p.getY();
        }

        double meanX = sumX / pointList.size();
        double meanY = sumY / pointList.size();

        double numeratorX = 0.0;
        double numeratorY = 0.0;

        for (Vertex p : pointList) {
            numeratorX += Math.pow(p.getX() - meanX, 2);
            numeratorY += Math.pow(p.getY() - meanY, 2);
        }

        double varianceX = numeratorX / pointList.size();
        double varianceY = numeratorY / pointList.size();

        //Calculate the Profile on the axis with the higher variance
        if (varianceX > varianceY) {
            for (Vertex p : pointList) {
                Vector2D point2D = new Vector2D(p.getIndex(), p.getX(), p.getZ());
                pointSet.add(point2D);
            }
        } else {
            for (Vertex p : pointList) {
                Vector2D point2D = new Vector2D(p.getIndex(), p.getY(), p.getZ());
                pointSet.add(point2D);
            }
        }

        return pointSet;
    }

    /** Extract the indices of 2D points from the 2D triangles and create 3D triangles from them. **/
    private List<Triangle> formatConnections(List<Triangle2D> triangleSoup) {
        List<Triangle> connections = new ArrayList<>();

        for (Triangle2D triangle : triangleSoup) {
            Triangle connection = new Triangle(triangle.a.id, triangle.b.id, triangle.c.id);
            connections.add(connection);
        }

        return connections;
    }

}
