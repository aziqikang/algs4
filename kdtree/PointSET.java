import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        StdDraw.setPenRadius(0.005);
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsInRectangle = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) pointsInRectangle.add(p);
        }

        return pointsInRectangle;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double minDistance = Integer.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D point2D : points) {
            if (point2D.distanceSquaredTo(p) < minDistance) {
                minDistance = point2D.distanceSquaredTo(p);
                nearest = point2D;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSet = new PointSET();
        In in = new In(args[0]);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pointSet.insert(p);
        }

        new Point2D(0.5, 0.5).draw();
        pointSet.draw();

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        System.out.println(pointSet.nearest(new Point2D(0.0, 0.0)));
        pointSet.nearest(new Point2D(0.0, 0.0)).draw();
        System.out.println(pointSet.nearest(new Point2D(0.7, 0.4)));
        pointSet.nearest(new Point2D(0.7, 0.4)).draw();
    }
}
