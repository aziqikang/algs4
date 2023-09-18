import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {

    private class Node implements Comparable<Node> {
        private Board current;
        private Node previous;
        private int moves;
        private final int priority;

        Node(Board c, Node p, int m) {
            current = c;
            previous = p;
            moves = m;
            priority = moves + current.manhattan();
        }

        public int compareTo(Node other) {
            return Integer.compare(this.priority,
                                   other.priority);
        }
    }

    private Node solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        // initialize pq and twinPQ
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();

        Node min = new Node(initial, null, 0);
        pq.insert(min);
        Node twinMin = new Node(initial.twin(), null, 0);
        twinPQ.insert(twinMin);


        while (!min.current.isGoal() && !twinMin.current.isGoal()) {
            min = pq.delMin();
            for (Board neighbor : min.current.neighbors()) {
                if (min.previous == null || !min.previous.current.equals(neighbor)) {
                    pq.insert(new Node(neighbor, min, min.moves + 1));
                }
            }

            twinMin = twinPQ.delMin();
            for (Board neighbor : twinMin.current.neighbors()) {
                if (twinMin.previous == null || !twinMin.previous.current.equals(neighbor)) {
                    twinPQ.insert(new Node(neighbor, twinMin, twinMin.moves + 1));
                }
            }
        }

        if (twinMin.current.isGoal()) { // not solvable
            solution = null;
        }
        else { // solvable
            solution = min;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return solution.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Node> stack = new Stack<>(); // temporary stack
        Node current = solution;
        while (current != null) {
            stack.push(current);
            current = current.previous;
        }

        ArrayList<Board> sBoards = new ArrayList<>();
        while (!stack.isEmpty()) {
            sBoards.add(stack.pop().current);
        }

        return sBoards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
