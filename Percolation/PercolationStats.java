import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            thresholds[i] = p.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(thresholds.length);
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);         // n-by-n percolation system
        int t = Integer.parseInt(args[1]);         // number of trials

        PercolationStats stats = new PercolationStats(n, t);

        System.out.printf("%-24s%s%.7f%n", "mean", "= ", stats.mean());
        System.out.printf("%-24s%s%.17f%n", "stddev", "= ", stats.stddev());
        System.out.printf("%-24s%s", "95% confidence interval", "= ");
        System.out.printf("%s%.16f%s%.16f%s", "[", stats.confidenceLo(), ",", stats.confidenceHi(),
                          "]");
    }

}
