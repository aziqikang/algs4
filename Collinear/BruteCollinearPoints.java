import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int n;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points

        if (points == null) throw new IllegalArgumentException("argument is null");
        
        for (int i = 0; i < points.length; i++) { // check for duplicate/null points
            if (points[i] == null) throw new IllegalArgumentException("array has null element");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("duplicate points");
            }
        }

        segments = new LineSegment[1];
        n = 0;

        if (points.length >= 4) {
            for (int i = 0; i < points.length; i++) {
                for (int j = i + 1; j < points.length; j++) {
                    for (int k = j + 1; k < points.length; k++) {
                        for (int l = k + 1; l < points.length; l++) {
                            if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                                    && points[i].slopeTo(points[k]) == points[i].slopeTo(points[l])) {
                                if (n == segments.length) resize(2 * n);
                                segments[n++] = segment(new Point[]{points[i], points[j], points[k], points[l]});
                            }
                        }
                    }
                }
            }
        }

    }

    // returns the line segment through four collinear points
    private LineSegment segment(Point[] collinearPoints) {
        if (collinearPoints.length != 4) throw new IllegalArgumentException("parameter should have length 4");
        Arrays.sort(collinearPoints);
        return new LineSegment(collinearPoints[0], collinearPoints[3]);
    }

    //resizing
    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        if (n >= 0) System.arraycopy(segments, 0, copy, 0, n);
        segments = copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return n;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[n];
        System.arraycopy(segments, 0, copy, 0, n);

        return copy;
    }

    public static void main(String[] args) {
        Point[] points = {new Point(1, 1), new Point(11, 10), new Point(0, 0), new Point(94, -94), new Point(3, 3),
                new Point(5, 4), new Point(12, 12), new Point(1, 0), new Point(3, 2)};
        BruteCollinearPoints collinearPoints = new BruteCollinearPoints(points);

        StdOut.println("number of segments: " + collinearPoints.numberOfSegments());
        // should print segments (0,0) -> (12, 12), (1, 0) -> (11, 10)
        for (LineSegment line : collinearPoints.segments()) {
            StdOut.println(line);
        }
    }
}
