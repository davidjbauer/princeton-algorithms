import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Deque;
import java.util.ArrayDeque;
/* API Specification:
public class Board {
    >public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    >public int dimension()                 // board dimension n
    >public int hamming()                   // number of blocks out of place
    >public int manhattan()                 // sum of Manhattan distances between blocks and goal
    >public boolean isGoal()                // is this board the goal board?
    >public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    >public boolean equals(Object y)        // does this board equal y?
    >public Iterable<Board> neighbors()     // all neighboring boards
    >public String toString()               // string representation of this board (in the output format specified below)

    public static void main(String[] args) // unit tests (not graded)
}
*/

public class Board {

    private int dim;
    private int[] board;
    private int[] goalBoard;

    public Board(int[][] blocks) {
        this.dim = blocks.length;
        int n = this.dim;
        this.board = new int[n*n];
        for (int i = 0; i < n*n; i++) {
            int row = i/n;
            int col = i%n;
            board[i] = blocks[row][col];
        }

        this.goalBoard = new int[n*n];
        for (int i = 0; i < n*n; i++) {
            this.goalBoard[i] = i + 1;
        }
        this.goalBoard[n*n-1] = 0;
    }

    public int dimension() {
        return this.dim;
    }

    public int hamming() {
        int dist = 0;
        for (int i = 0; i < this.dim*this.dim; i++) {
            if (this.board[i] != this.goalBoard[i] && this.board[i] != 0)
                dist++;
        }
        return dist;
    }

    public int manhattan() {
        int dist = 0;
        int diff, xDist, yDist;
        for (int i = 0; i < this.dim*this.dim; i++) {
            if (this.board[i] != 0) {
                diff = this.board[i] - (i + 1);
                xDist = diff % this.dim;
                yDist = diff / this.dim;
                dist = dist + Math.abs(xDist) + Math.abs(yDist);
            }
        }
        return dist;
    }
    private int[][] reshape2d(int[] input) {
        int[][] blocks = new int[this.dim][this.dim];
        int n = this.dim;
        for (int i = 0; i < n*n; i++) {
            int row = i/n;
            int col = i%n;
            blocks[row][col] = input[i];
        }
        return blocks;
    }

    public Iterable<Board> neighbors() {
        Deque<Board> stack = new ArrayDeque<Board>();
        int n = this.dim;
        int emptyLoc = -1;
        int row, col, nRow, nCol;
        int[][] tmpArray;
        for (int i = 0; i < n*n; i++) {
            if (board[i] == 0)
                emptyLoc = i;
        }
        row = emptyLoc / n;
        col = emptyLoc % n;
        final int[] neighborSites = {emptyLoc + 1, emptyLoc - 1,
                                     emptyLoc + n, emptyLoc - n};
        for (int site : neighborSites) {
            nRow = site / n;
            nCol = site % n;
            if (site >= 0 && site < n*n && (row == nRow || col == nCol)) {
                tmpArray = this.reshape2d(this.board);
                tmpArray[row][col] = this.board[site];
                tmpArray[nRow][nCol] = this.board[emptyLoc];
                stack.push(new Board(tmpArray));
            }
        }
        return stack;
    }

    public boolean isGoal() {
        boolean goal = true;
        for (int i = 0; i < this.dim*this.dim; i++) {
            goal &= (board[i] == goalBoard[i]);
        }
        return goal;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        // loop over rows
        sb.append(String.format("%d\n", this.dim));
        for (int i = 0; i < this.dim; i++) {
            // loop over columns
            for (int j = 0; j < this.dim; j++) {
                sb.append(String.format("%2d ", board[this.dim*i + j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        Board bOther = (Board) other;
        boolean result = true;
        for (int i = 0; i < this.dim*this.dim; i++) {
            result &= (this.board[i] == bOther.board[i]);
        }
        return result;
    }

    public Board twin() {
        int n = this.dim;
        int[][] newBoard = this.reshape2d(this.board);
        int rowThis, colThis, rowNext, colNext;
        // Loop through all the sites and find two neighboring sites in the same
        // row that are non-empty.
        for (int i = 0; i < n*n; i++) {
            rowThis = i / n;
            colThis = i % n;
            rowNext = (i+1) / n;
            colNext = (i+1) % n;
            if (this.board[i] != 0 && this.board[i+1] != 0 && rowThis == rowNext) {
                newBoard[rowThis][colThis] = this.board[i+1];
                newBoard[rowNext][colNext] = this.board[i];
                break;
            }
        }
        return new Board(newBoard);
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Board same = new Board(blocks);
        StdOut.println(initial);
        StdOut.println("Initial board equals a copy of itself?");
        StdOut.println(initial.equals(same));
        StdOut.println("Manhattan distance: ");
        StdOut.println(initial.manhattan());
        StdOut.println("Hamming distance: ");
        StdOut.println(initial.hamming());
        StdOut.println("Neighboring boards: ");
        Iterable<Board> neighborBoards = initial.neighbors();
        for (Board nB : neighborBoards) {
            StdOut.println(nB);
            StdOut.println("Neighbor equals initial?");
            StdOut.println(initial.equals(nB));
        }
        Board twinBoard = initial.twin();
        StdOut.println("Twin board: ");
        StdOut.println(twinBoard);

    }
}
