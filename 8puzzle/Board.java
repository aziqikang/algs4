import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private int[][] tiles;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                this.tiles[row][col] = tiles[row][col];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = tiles[row][col];
                if (tile != 0 && tile != n * row + col + 1) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = tiles[row][col];
                if (tile != 0) {
                    int r = (tile - 1) / n; // the final column location of this tile
                    int c = tile - 1 - n * r; // the final row location of this tile
                    count += Math.abs(row - r) + Math.abs(col - c);
                }
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board other = (Board) y;
        return Arrays.deepEquals(this.tiles, other.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // find 'empty' tile
        int emptyRow = 0, emptyCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) {
                    emptyRow = row;
                    emptyCol = col;
                }
            }
        }

        if (emptyCol + 1 < n) {
            neighbors.add(neighbor(emptyRow, emptyCol, emptyRow, emptyCol + 1));
        }
        if (emptyCol - 1 >= 0) {
            neighbors.add(neighbor(emptyRow, emptyCol, emptyRow, emptyCol - 1));
        }
        if (emptyRow + 1 < n) {
            neighbors.add(neighbor(emptyRow, emptyCol, emptyRow + 1, emptyCol));
        }
        if (emptyRow - 1 >= 0) {
            neighbors.add(neighbor(emptyRow, emptyCol, emptyRow - 1, emptyCol));
        }

        return neighbors;
    }

    private Board neighbor(int tile1Row, int tile1Col, int tile2Row, int tile2Col) {
        int[][] neighbor = new int[n][n];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                neighbor[row][col] = tiles[row][col];
            }
        }
        int tile1 = tiles[tile1Row][tile1Col];
        neighbor[tile1Row][tile1Col] = neighbor[tile2Row][tile2Col];
        neighbor[tile2Row][tile2Col] = tile1;

        return new Board(neighbor);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // copy the original array
        int[][] twin = new int[n][n];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                twin[row][col] = tiles[row][col];
            }
        }


        // find and swap two distinct nonempty tiles
        int xRow = 0, xCol = 0, yRow = 0, yCol = 0;
        tileX:
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (tiles[row][col] != 0) {
                    xRow = row;
                    xCol = col;
                    break tileX;
                }
            }
        }

        tileY:
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (tiles[row][col] != 0 && (row != xRow || col != xCol)) {
                    yRow = row;
                    yCol = col;
                    break tileY;
                }
            }
        }
        int tileX = tiles[xRow][xCol];
        twin[xRow][xCol] = twin[yRow][yCol];
        twin[yRow][yCol] = tileX;

        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][] { { 1, 0, 3 }, { 4, 6, 7 }, { 2, 8, 5 } });
        System.out.print(board);
        System.out.println("hamming: " + board.hamming() + "\nmanhattan: " + board.manhattan());

        System.out.println("\nneighbors:\n");
        for (Board b : board.neighbors()) {
            System.out.print(b);
            System.out.println("hamming: " + b.hamming() + "\nmanhattan: " + b.manhattan());
            System.out.println();
        }

        System.out.println("twin:\n" + board.twin());
    }

}
