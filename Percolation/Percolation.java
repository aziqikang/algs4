/**
 * Algorithms Week 1 Project -- Percolation
 *
 *  [desc]
 *
 * @author Amy Kang
 *
 * @version September 23, 2021
 */
public class Percolation {

    private int numOpenSites;
    private WeightedQuickUnionUF uf; // represents all sites, includes a top and a bottom node
    private WeightedQuickUnionUF partialUf; // does not include top node, used for backwash issues
    private boolean[][] sites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        numOpenSites = 0;
        sites = new boolean[n + 2][n + 2];
        uf = new WeightedQuickUnionUF(n * n + 2); // all the sites in sites,
        // excluding the buffer and including two nodes for the top and bottom
        partialUf = new WeightedQuickUnionUF(n * n + 1);
        for (int r = 0; r < n + 2; r++) {
            for (int c = 0; c < n + 2; c++) {
                sites[r][c] = false;
            }
        }
    }

    // helper, maps row, col in sites to a 1-d index for quick-union

    /**
     * Helper method that maps the site at (row, col) to it's corresponding
     * index in the 1-dimensional matrix where the sites[][] is collapsed
     *
     * @param row row index of given site
     * @param col column index of given site
     * @return an int representing the index of the site when collapsed to a 1-d matrix
     */
    private int map(int row, int col) {
        return (sites.length - 2) * (row - 1) + col;
    }

    /**
     * Opens the site at (row, col) if it is not already open
     *
     * @param row row index of given site
     * @param col column index of given site
     */
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > sites.length - 2 || col > sites.length - 2)
            throw new IllegalArgumentException();

        // if site is already open, do nothing
        if (sites[row][col]) return;

        sites[row][col] = true;
        // check adjacent sites -- if open, connect the two
        if (sites[row + 1][col]) {
            uf.union(map(row + 1, col), map(row, col));
            partialUf.union(map(row + 1, col), map(row, col));
        }
        if (sites[row - 1][col]) {
            uf.union(map(row - 1, col), map(row, col));
            partialUf.union(map(row - 1, col), map(row, col));
        }
        if (sites[row][col + 1]) {
            uf.union(map(row, col + 1), map(row, col));
            partialUf.union(map(row, col + 1), map(row, col));
        }
        if (sites[row][col - 1]) {
            uf.union(map(row, col - 1), map(row, col));
            partialUf.union(map(row, col - 1), map(row, col));
        }

        // if this site is in row 1 or row n, connect to top or bottom row, respectively
        if (row == 1) {
            uf.union(map(row, col), 0);
            partialUf.union(map(row, col), 0);
        }
        if (row == sites.length - 2) {
            uf.union(map(row, col), (sites.length - 2) * (sites.length - 2) + 1);
        }
        numOpenSites++;
    }

    /**
     * Calculates and returns whether a given site is open
     *
     * @param row row index of given site
     * @param col column index of given site
     * @return a boolean representing whether the site is open
     */
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > sites.length - 2 || col > sites.length - 2)
            throw new IllegalArgumentException();
        return sites[row][col];
    }


    /**
     * Uses UnionFind to calculate and return if the given site is full
     *
     * @param row row index of given site
     * @param col column index of given site
     * @return a boolean representing whether the site is percolated through
     */
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > sites.length - 2 || col > sites.length - 2)
            throw new IllegalArgumentException();
        return partialUf.find(map(row, col)) == partialUf.find(0);
    }

    /**
     * references the number of open sites
     *
     * @return an int representing the number of open sites
     */
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    /**
     * returns whether the object percolates
     *
     * @return a boolean representing whether the object percolates
     */
    public boolean percolates() {
        return uf.find((sites.length - 2) * (sites.length - 2) + 1) == uf.find(0);
    }
}
