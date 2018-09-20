import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Deque;
import java.util.ArrayDeque;
import edu.princeton.cs.algs4.MinPQ;
/* API Specification
public class Solver {
    > public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    > public boolean isSolvable()            // is the initial board solvable?
    > public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    > public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    > public static void main(String[] args) // solve a slider puzzle (given below)
}
*/

public class Solver {
    private Deque<Board> stack;
    private MinPQ<SearchNode> pq;
    private int numMoves;
    private boolean canSolve;

    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        private int priority;
        public SearchNode predNode;
        public SearchNode(Board b, int p, SearchNode pr) {
            this.board = b;
            this.priority = p;
            this.predNode = pr;
        }
        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            else if (this.priority > that.priority) return 1;
            else return 0;
        }
    }

    public Solver(Board initial) {
        this.stack = new ArrayDeque<Board>();
        this.numMoves = 0;
        Board currBoard = initial;
        Board twinBoard = initial.twin();
        if (initial.isGoal()) {
            this.stack.push(currBoard);
            this.canSolve = true;
        }
        else if (twinBoard.isGoal()) {
            this.stack.push(twinBoard);
            this.canSolve = false;
        }
        else {
            Iterable<Board> neighbors;
            SearchNode currNode = new SearchNode(currBoard, initial.manhattan(), null);
            SearchNode twinNode = new SearchNode(twinBoard, twinBoard.manhattan(), null); 
            MinPQ<SearchNode> initPQ = new MinPQ<SearchNode>();
            MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
            //initPQ.insert(currNode);
            //twinPQ.insert(twinNode);
            while(!currBoard.isGoal() && !twinBoard.isGoal()) {
                ++numMoves;
                neighbors = currBoard.neighbors();
                for (Board n : neighbors) {
                    if (currNode.predNode == null)
                        initPQ.insert(new SearchNode(n, n.manhattan() + numMoves, currNode));
                    else if (!n.equals(currNode.predNode.board))
                        initPQ.insert(new SearchNode(n, n.manhattan() + numMoves, currNode));
                }
                currNode = initPQ.delMin();
                currBoard = currNode.board;
                neighbors = twinBoard.neighbors();
                for (Board n : neighbors) {
                    if (twinNode.predNode == null)
                        twinPQ.insert(new SearchNode(n, n.manhattan() + numMoves, twinNode));
                    else if (!n.equals(twinNode.predNode.board))
                        twinPQ.insert(new SearchNode(n, n.manhattan() + numMoves, twinNode));
                }
                twinNode = twinPQ.delMin();
                twinBoard = twinNode.board;
            }
            numMoves = -1;
            if (currBoard.isGoal()) {
                this.canSolve = true;
                while (currNode != null){
                    numMoves++;
                    stack.push(currNode.board);
                    currNode = currNode.predNode;
               } 
            } 
            else if (twinBoard.isGoal()) this.canSolve = false;
        }
    }

    public boolean isSolvable() {
        return this.canSolve;
    }

    public int moves() {
        return this.numMoves;
    }

    public Iterable<Board> solution() {
        return this.stack;
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