import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
    }

    // validate vertex
    private void validateVertex(int v) {
        if (!(v >= 0 && v < digraph.V())) {
            throw new IllegalArgumentException("invalid vertex");
        }
    }

    private int validateVertices(Iterable<Integer> v) {
        if (v == null) throw new IllegalArgumentException("null iterable");
        int sizeV = 0;
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException("iterable contains null vertex");
            validateVertex(i);
            sizeV++;
        }
        return sizeV;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int pathLength = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (pathLength < minDistance) {
                    minDistance = pathLength;
                }
            }
        }

        if (minDistance != Integer.MAX_VALUE) return minDistance;

        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        int sca = -1;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int pathLength = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (pathLength < minDistance) {
                    minDistance = pathLength;
                    sca = vertex;
                }
            }
        }

        return sca;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (validateVertices(v) == 0 || validateVertices(w) == 0) {
            return -1;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int pathLength = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (pathLength < minDistance) {
                    minDistance = pathLength;
                }
            }
        }

        if (minDistance != Integer.MAX_VALUE) return minDistance;
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (validateVertices(v) == 0 || validateVertices(w) == 0) {
            return -1;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int minDistance = Integer.MAX_VALUE;
        int sca = -1;

        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int pathLength = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (pathLength < minDistance) {
                    minDistance = pathLength;
                    sca = vertex;
                }
            }
        }

        return sca;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
