import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
/* API Specification:
public class Board {
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    public int hamming()                   // number of blocks out of place
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    public boolean isGoal()                // is this board the goal board?
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    public boolean equals(Object y)        // does this board equal y?
    public Iterable<Board> neighbors()     // all neighboring boards
    public String toString()               // string representation of this board (in the output format specified below)

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
            if (board[i] != 0) {
                diff = this.board[i] - (i + 1);
                xDist = diff % this.dim;
                yDist = diff / this.dim;
                dist = dist + Math.abs(xDist) + Math.abs(yDist);
            }
        }
        return dist;
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
        for (int i = 0; i < this.dim; i++) {
            // loop over columns
            for (int j = 0; j < this.dim; j++) {
                sb.append(board[this.dim*i + j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
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

        StdOut.println(initial);
        StdOut.println(initial.manhattan());
        StdOut.println(initial.hamming());
    }
}
