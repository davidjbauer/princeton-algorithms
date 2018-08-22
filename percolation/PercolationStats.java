import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int n;
    private int trials;
    private double[] thresholds;
    private int randomX, randomY;
    private double rootTrials;

    public PercolationStats(int m, int t) {
        if (m <= 0 || t <= 0)
            throw new java.lang.IllegalArgumentException("Grid size and number of trials must be > 0");
        n = m;
        trials = t;
        thresholds = new double[trials];
        rootTrials = java.lang.Math.sqrt(trials);

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                randomX = StdRandom.uniform(1, n+1);
                randomY = StdRandom.uniform(1, n+1);
                if (!p.isOpen(randomX, randomY))
                    p.open(randomX, randomY);
            }
            thresholds[i] = p.numberOfOpenSites()*1.0/(n*n);
        }
    }                
    
    public double mean() 
    {
        return StdStats.mean(thresholds);
    }

    public double stddev() 
    {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLow() 
    {
        return this.mean() - 1.96*this.stddev()/rootTrials;
    }


    public double confidenceHigh() 
    {
        return this.mean() + 1.96*this.stddev()/rootTrials;
    }

    // test client
    public static void main(String[] args)
    {
        int n = java.lang.Integer.parseInt(args[0]);
        int trials = java.lang.Integer.parseInt(args[1]);
        double t1, t2;
        Stopwatch timer = new Stopwatch();
        StdOut.printf("%d trials, %d-by-%d grid\n", trials, n, n);
        t1 = timer.elapsedTime();
        PercolationStats stats = new PercolationStats(n, trials);
        t2 = timer.elapsedTime();
        StdOut.printf("Runtime:    %f\n", t2-t1);
        StdOut.printf("----------\n", n);
        StdOut.printf("Mean:       %f\n", stats.mean());
        StdOut.printf("StdDev:     %f\n", stats.stddev());
        StdOut.printf("95ConfLow:  %f\n", stats.confidenceLow());
        StdOut.printf("95ConfHigh: %f\n", stats.confidenceHigh());


    }   
}