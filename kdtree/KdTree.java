import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {

    private static class Node {
        private Point2D p;                      // the point
        private RectHV rect;
        // the partition of the unit square this node is contained in
        private Node leftBottomSubtree;         // the left/bottom subtree
        private Node rightUpperSubtree;         // the right/top subtree
        private boolean isVertical;
    }

    private final Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = new Node();
        root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        root.isVertical = true;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node node = insertVertical(p, root);
        node.p = p;
    }

    private Node insertVertical(Point2D p, Node node) {
        if (node.p == null) {
            node.leftBottomSubtree = new Node();
            node.leftBottomSubtree.isVertical = false;
            node.leftBottomSubtree.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), p.x(),
                                                     node.rect.ymax());
            node.rightUpperSubtree = new Node();
            node.rightUpperSubtree.isVertical = false;
            node.rightUpperSubtree.rect = new RectHV(p.x(), node.rect.ymin(), node.rect.xmax(),
                                                     node.rect.ymax());
            size++;
            return node;
        }
        if (node.p.equals(p)) {
            return node;
        }

        // else
        if (p.x() < node.p.x()) { // point in left subtree
            return insertHorizontal(p, node.leftBottomSubtree);
        }
        else { // point in right subtree
            return insertHorizontal(p, node.rightUpperSubtree);
        }
    }

    private Node insertHorizontal(Point2D p, Node node) {
        if (node.p == null) {
            node.leftBottomSubtree = new Node();
            node.leftBottomSubtree.isVertical = true;
            node.leftBottomSubtree.rect = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                                     node.rect.xmax(), p.y());
            node.rightUpperSubtree = new Node();
            node.rightUpperSubtree.isVertical = true;
            node.rightUpperSubtree.rect = new RectHV(node.rect.xmin(), p.y(), node.rect.xmax(),
                                                     node.rect.ymax());
            size++;
            return node;
        }
        if (node.p.equals(p)) {
            return node;
        }

        // else
        if (p.y() < node.p.y()) { // point in bottom subtree
            return insertVertical(p, node.leftBottomSubtree);
        }
        else { // point in upper subtree
            return insertVertical(p, node.rightUpperSubtree);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node.p == null) {
            return false;
        }
        if (node.p.equals(p)) {
            return true;
        }

        if ((node.isVertical && p.x() < node.p.x()) || (!node.isVertical && p.y() < node.p.y())) {
            return contains(node.leftBottomSubtree, p);
        }
        else {
            return contains(node.rightUpperSubtree, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        draw(root);
    }

    private void draw(Node node) {
        if (node.p != null) {
            StdDraw.setPenRadius(0.002);
            if (node.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(node.p.x(), node.rect.ymin())
                        .drawTo(new Point2D(node.p.x(), node.rect.ymax()));
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                new Point2D(node.rect.xmin(), node.p.y())
                        .drawTo(new Point2D(node.rect.xmax(), node.p.y()));
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

            draw(node.leftBottomSubtree);
            draw(node.rightUpperSubtree);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<>();
        if (isEmpty()) return points;

        range(root, rect, points);

        return points;
    }

    private void range(Node current, RectHV rect, ArrayList<Point2D> points) {
        if (rect.contains(current.p)) points.add(current.p);

        // search subtrees if they intersect rect
        if (current.leftBottomSubtree.p != null && current.leftBottomSubtree.rect
                .intersects(rect)) {
            range(current.leftBottomSubtree, rect, points);
        }
        if (current.rightUpperSubtree.p != null && current.rightUpperSubtree.rect
                .intersects(rect)) {
            range(current.rightUpperSubtree, rect, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D min = root.p;

        return nearest(min, p, root);
    }

    private Point2D nearest(Point2D min, Point2D p, Node current) {
        if (current.p.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
            min = current.p;
        }

        // search subtrees if they could contain the nearest point
        if (current.leftBottomSubtree.p != null
                && current.leftBottomSubtree.rect.distanceSquaredTo(p) < min
                .distanceSquaredTo(p)) {
            min = nearest(min, p, current.leftBottomSubtree);
        }
        if (current.rightUpperSubtree.p != null
                && current.rightUpperSubtree.rect.distanceSquaredTo(p) < min
                .distanceSquaredTo(p)) {
            min = nearest(min, p, current.rightUpperSubtree);
        }

        return min;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        In in = new In(args[0]);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        System.out.println("size: " + kdTree.size());
        System.out.println(
                "contains (0,0): " + kdTree.contains(new Point2D(0.0, 0.0)));   // false
        System.out.println(
                "contains (0.5,0): " + kdTree.contains(new Point2D(0.5, 0.0))); // true
        System.out.println("contains (0.024472, 0.654508): " + kdTree
                .contains(new Point2D(0.024472, 0.654508)));   // true

        // test for range
        System.out.println("\nTEST FOR RANGE");
        System.out.println("Points in bottom left quadrant: ");
        for (Point2D p : kdTree.range(new RectHV(0.0, 0.0, 0.5, 0.5))) {
            System.out.println(p);
        }
        System.out.println("Points in upper right quadrant: ");
        for (Point2D p : kdTree.range(new RectHV(0.5, 0.5, 1.0, 1.0))) {
            System.out.println(p);
        }

        // test for nearest
        System.out.println("\nTEST FOR NEAREST");
        System.out.println("nearest to origin: " + kdTree.nearest(new Point2D(0.0, 0.0)));
        System.out.println("nearest to (0.3, 0.7): " + kdTree
                .nearest(new Point2D(0.3, 0.7)));      // (0.206107, 0.904508)
        System.out.println("nearest to (0.8, 0.15): " + kdTree
                .nearest(new Point2D(0.8, 0.15)));    // (0.793893, 0.095492)

        kdTree.draw();

        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        new Point2D(0.3, 0.7).draw();
        new Point2D(0.8, 0.15).draw();
    }
}
