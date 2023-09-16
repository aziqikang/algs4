import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private int n;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("argument is null");
        for (int i = 0; i < points.length; i++) { // check for duplicate/null points
            if (points[i] == null) throw new IllegalArgumentException("array has null element");
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("duplicate points");
            }
        }


        segments = new LineSegment[1];
        n = 0;

        if (points.length > 4) {
            Point[] collinearPoints = new Point[points.length - 1];
            for (Point p : points) {

                // copy all other points into collinearPoints
                int i = 0;
                for (Point q : points) {
                    if (p.compareTo(q) != 0) {
                        collinearPoints[i++] = q;
                    }
                }

                // sort other points by coordinate
                MergeX.sort(collinearPoints);
                // sort other points by slope
                MergeX.sort(collinearPoints, p.slopeOrder());
                

                // search for at least three lines with the same slope to p
                double slope = p.slopeTo(collinearPoints[0]);
                int numPoints = 0; // current number of points with the same slope to p


                for (int j = 0; j < collinearPoints.length; j++) {
                    if (p.slopeTo(collinearPoints[j]) == slope && numPoints > 0) {
                        numPoints++;
                    } else if (p.slopeTo(collinearPoints[j]) != slope) {
                        if (numPoints >= 3) {
                            if (n == segments.length) resize(2 * n);
                            segments[n++] = new LineSegment(p, collinearPoints[j - 1]);
                        }

                        if (p.compareTo(collinearPoints[j]) > 0) {
                            numPoints = 0; // p is not the 'minimum' point
                        } else {
                            numPoints = 1;
                        }

                        slope = p.slopeTo(collinearPoints[j]);
                    }

                    // corner cases
                    if (j == 0 && p.compareTo(collinearPoints[0]) < 0) {
                        numPoints = 1;
                    }
                    if (j == collinearPoints.length - 1 && numPoints >= 3) {
                        if (n == segments.length) resize(2 * n);
                        segments[n++] = new LineSegment(p, collinearPoints[j]);
                    }
                }


            }
        }

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
//        Point[] points = {new Point(1, 1), new Point(11, 10), new Point(0, 0), new Point(94, -94), new Point(3, 3),
//                new Point(5, 4), new Point(12, 12), new Point(1, 0), new Point(3, 2)};
//        FastCollinearPoints collinearPoints = new FastCollinearPoints(points);
//
//        StdOut.println("number of segments: " + collinearPoints.numberOfSegments());
//        // should print segments (0,0) -> (12, 12), (1, 0) -> (11, 10)
//        for (LineSegment line : collinearPoints.segments()) {
//            StdOut.println(line);
//        }

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println("number of segments: " + collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
