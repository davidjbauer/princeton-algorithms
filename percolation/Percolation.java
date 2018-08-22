import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.Stopwatch;

public class Percolation {
   
    // Dimension of grid is n by n.
    private int n;
    private WeightedQuickUnionUF sites;
    private boolean[] siteOpen;
    private int numOpen;
    private int sourceIndex, sinkIndex;


    // create m-by-m grid, with all sites blocked
    public Percolation(int m) {
        if (m <= 0)
            throw new java.lang.IllegalArgumentException("Grid size must be positive, non-zero integer.");
        else {
            n = m;
            numOpen = 0;
            sourceIndex = n*n;
            sinkIndex = n*n + 1;
            // create our grid, plus two extra sites connected to the top and bottom rows, respectively.
            sites = new WeightedQuickUnionUF(n*n+2);
            siteOpen = new boolean[n*n+2];

            // O(n) so doesn't change O(n^2) runtime of constructor
            for (int i = 1; i <= n; i++) {
                sites.union(siteIndex(1, i), sourceIndex);
                sites.union(siteIndex(n, i), sinkIndex);
            }
            siteOpen[sourceIndex] = true;
            siteOpen[sinkIndex] = true;
        }
    }                
    
    // get internal index for site at (row, col).
    // Grid is indexed from 1 to n in each direction, we'll use 0 to n-1 indexing interally   
    private int siteIndex(int row, int col)
    {
        validateRowCol(row, col);
        return n*(row - 1) + (col-1);
    }

    // for finding nearest neighbors
    private int siteIndexUnsafe(int row, int col)
    {
        return n*(row - 1) + (col-1);
    }

    // return an array containing indices of neighbors of a given site
    // some elements of the array may not be valid indices, we will
    // need to check for this when using results of nearestNeighbors
    private int[] nearestNeighbors(int row, int col)
    {
        validateRowCol(row, col);
        int[] neighbors = new int[] {siteIndexUnsafe(row+1, col),
                                     siteIndexUnsafe(row, col+1),
                                     siteIndexUnsafe(row-1, col),
                                     siteIndexUnsafe(row, col-1)};
        return neighbors;
    }

    private boolean validIndex(int index)
    {
        if(index >= 0 && index <= n*n-1)
            return true;
        else
            return false;
    }

    private void validateRowCol(int row, int col)
    {
        if (row < 1 || row > n || col < 1 || col > n) {
            StdOut.printf("row = %d, col = %d\n", row, col);
            throw new java.lang.IllegalArgumentException("Row and column must be between 1 and n.");
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int index = siteIndex(row, col);
        // StdOut.printf("Opening (%d, %d), index %d\n", row, col, index);
        int[] siteNeighbors = nearestNeighbors(row, col);

        // set the site's status to open
        siteOpen[index] = true;

        // connect to all open neighbors
        // constant time because number of neighbors <= 4.
        for (int neighbor : siteNeighbors) {
            if (validIndex(neighbor) && siteOpen[neighbor]) {
                sites.union(index, neighbor);
            }
        }
        numOpen++;
    }    
   
    // is site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        int index = siteIndex(row, col);
        return siteOpen[index];
    }
   
   // is site (row, col) full?
    public boolean isFull(int row, int col) 
    {
        int index = siteIndex(row, col);
        return siteOpen[index] && sites.connected(index, sourceIndex);
    } 
   
    // number of open sites
    public int numberOfOpenSites()
    {
        return numOpen;
    }       
   
   // does the system percolate?
    public boolean percolates()
    {
        return sites.connected(sourceIndex, sinkIndex);
    }         

   // test client
    public static void main(String[] args)
    {
        double t1, t2;
        Stopwatch timer = new Stopwatch();

        t1 = timer.elapsedTime();
        Percolation p4 = new Percolation(4);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(4) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p128 = new Percolation(128);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(128) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p256 = new Percolation(256);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(256) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p512 = new Percolation(512);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(512) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p1024 = new Percolation(1024);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(1024) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p2048 = new Percolation(2048);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(2048) took %f\n",t2-t1);

        t1 = timer.elapsedTime();
        Percolation p5096 = new Percolation(5096);
        t2 = timer.elapsedTime();
        StdOut.printf("Percolation(5096) took %f\n",t2-t1);

        StdOut.println("All sites of p4 should be blocked.");
        StdOut.printf("Number open: %d\n", p4.numberOfOpenSites());
        StdOut.printf("p4 percolates? %b\n", p4.percolates());
        StdOut.printf("(1, 1) open? %b full? %b\n", p4.isOpen(1, 1), p4.isFull(1, 1));
        StdOut.printf("(2, 3) open? %b full? %b\n", p4.isOpen(2, 3), p4.isFull(2, 3));
        StdOut.println("Opening site (1, 3), (2, 3), (2, 2), (3, 2), (4, 2).");
        p4.open(1, 1);
        p4.open(1, 3);
        p4.open(2, 3);
        p4.open(2, 2);
        p4.open(3, 2);
        p4.open(4, 2);
        StdOut.printf("Number open: %d\n", p4.numberOfOpenSites());
        StdOut.printf("p4 percolates? %b\n", p4.percolates());

    }   
}