import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   
    // Dimension of grid is n by n.
    private int n;
    private WeightedQuickUnionUF sites;
    private boolean[] siteOpen;
    private int numOpen;
    private int sourceIndex, sinkIndex;

    // create m-by-m grid, with all sites blocked
    public Percolation(int m) {
        if (m <= 0) {
            throw new java.lang.IllegalArgumentException("Grid size must be positive, non-zero integer.");
        }
        else {
            n = m;
            numOpen = 0;
            sourceIndex = n*n;
            sinkIndex = n*n + 1;
            // create our grid, plus two extra sites connected
            // to the top and bottom rows, respectively.
            sites = new WeightedQuickUnionUF(n*n+2);
            siteOpen = new boolean[n*n+2];

            for (int i = 1; i <= n; i++) {
                sites.union(siteIndex(1, i), sourceIndex);
            }
            siteOpen[sourceIndex] = true;

            for (int i = 1; i <= n; i++) {
                sites.union(siteIndex(n, i), sinkIndex);
            }
            siteOpen[sinkIndex] = true;
        }
    }                
    
    // get internal index for site at (row, col).
    // recall that grid is indexed from 1 to n in each direction
    // we'll use 0 to n-1 indexing interally   
    private int siteIndex(int row, int col)
    {
        validateRowCol(row, col);
        return (row - 1) + n*(col-1);
    }

    // for finding nearest neighbors
    private int siteIndexUnsafe(int row, int col)
    {
        return 0;
    }

    // return an array containing indices of neighbors of a given site
    // doing this by brute force for now, I'm sure there's a better way to do this
    private int[] nearestNeighbors(int row, int col)
    {
        validateRowCol(row, col);
        int[] neighbors;
        int index = siteIndex(row, col);
        // corners have two neighbors
        if (index == siteIndex(1,1)) {
            neighbors = new int[] {siteIndex(row, col+1), siteIndex(row+1, col)};
        }
        else if (index == siteIndex(1,n)) {
            neighbors = new int[] {siteIndex(row+1, col), siteIndex(row, col-1)};
        }
        else if (index == siteIndex(n,1)) {
            neighbors = new int[] {siteIndex(row-1, col), siteIndex(row, col+1)};
        }
        else if (index == siteIndex(n,n)) {
            neighbors = new int[] {siteIndex(row-1, col), siteIndex(row, col -1)};
        }
        // sites on edges have three neighbors
        else if (row == 1) {
            neighbors = new int[] {siteIndex(row, col-1), siteIndex(row, col+1), siteIndex(row+1, col)};
        }
        else if (row == n) {
            neighbors = new int[] {siteIndex(row, col-1), siteIndex(row, col+1), siteIndex(row-1, col)};
        }
        else if (col == 1) {
            neighbors = new int[] {siteIndex(row-1, col), siteIndex(row+1, col), siteIndex(row, col+1)};
        }
        else if (col == n) {
            neighbors = new int[] {siteIndex(row-1, col), siteIndex(row+1, col), siteIndex(row, col-1)};
        }
        // Interior sites have four neighbors
        else {
            neighbors = new int[] {siteIndex(row-1, col), siteIndex(row, col+1), siteIndex(row+1, col), siteIndex(row, col-1)};
        }

        return neighbors;

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
        int[] siteNeighbors = nearestNeighbors(row, col);

        // set the site's status to open
        siteOpen[index] = true;

        // connect to all open neighbors
        for (int neighbor : siteNeighbors) {
            if (siteOpen[neighbor]) {
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
        boolean full = false;
        int[] neighbors = nearestNeighbors(row, col);
        boolean connectedToNeighbor = false;

        if (siteOpen[index]) {
            full = sites.connected(index, sourceIndex);
            // for (int neighborIndex : neighbors) {
            //     connectedToNeighbor |= sites.connected(index,neighborIndex);
            // }
            // full &= connectedToNeighbor;
        }

        return full;
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
        StdOut.println("Percolation.")
    }   
}