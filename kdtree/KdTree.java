import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.ArrayDeque;

    
// API Specification 
// public class PointSET {
//    public         PointSET()                               // construct an empty set of points 
//    public           boolean isEmpty()                      // is the set empty? 
//    public               int size()                         // number of points in the set 
//    public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
//    public           boolean contains(Point2D p)            // does the set contain point p? 
//    public              void draw()                         // draw all points to standard draw 
//    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary) 
//    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 

//    public static void main(String[] args)                  // unit testing of the methods (optional) 
// }

public class KdTree {

    // a wrapper for Point2D that stores direction
    // of the associated line segment. we'll use a boolean with
    // false ~ vertical
    // true ~ horizontal.
    //  
    private static final boolean VERT = false;
    private static final boolean HORIZ = true;
    private class KdTreeNode implements Comparable<KdTreeNode>  {
        public boolean orientation;
        public Point2D pt;
        public KdTreeNode left, right;
        public RectHV rect;
        public KdTreeNode(boolean o, Point2D p, RectHV r) {
            this.left = this.right = null;
            this.pt = p;
            this.orientation = o;
            this.rect = r;
        }
        public int compareTo(KdTreeNode that) {
            // first node is vertically oriented
            // compare by x coordinate
            if (this.orientation == VERT) {
                if (this.xVal < that.xVal)
                    return -1;
                else if (this.xVal > that.xVal)
                    return 1;
                else return 0;
            }
            // first node horizontally oriented,
            // compare by y coordinate
            else {
                if (this.yVal < that.yVal)
                    return -1;
                else if (this.yVal > that.yVal)
                    return 1;
                else return 0; 
            }
        }
    }
    private KdTreeNode root;
    private int size;

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        if(this.root == null) return true;
        else return false;
    }

    public int size() {
        return this.size;
    }

    // Slightly modified version of recursive BST insert
    // borrowed from Algs4 lectures.
    private KdTreeNode insert(KdTreeNode orig, Point2D insertPt, boolean currOrient, RectHV currRect) {
        RectHV newRect;
        KdTreeNode curr = orig;
        KdTreeNode toAdd = new KdTreeNode(!currOrient, insertPt, currRect);
        if (curr == null) {
            ++this.size;
            return toAdd;
        }
        int comp = curr.compareTo(toAdd);
        if (comp < 0) {
            if (currOrient == VERT)
                newRect = 
            else if (currOrient == HORIZ)
                newRect = 
            curr.left = insert(curr.left, p, !currOrient);
        }
        else if (comp >= 0) {
            newRect = 
            curr.right = insert(curr.right, p, !currOrient);
        }
        return curr;
    }

    public void insert(Point2D p) {
        this.root = insert(this.root, p, VERT, new RectHV(0, 0 , 1, 1));
    }

    // Private preorder traversal helper function
    private void preorder(KdTreeNode orig, ArrayDeque<KdTreeNode> q) {
        if (orig == null) return;
        q.add(orig);
        preorder(orig.left, q);
        preorder(orig.right, q);
    }
    // Private iterator for drawing, etc.
    private Iterable<KdTreeNode> iter() {
        ArrayDeque<KdTreeNode> q = new ArrayDeque<KdTreeNode>();
        preorder(this.root, q);
        return q;
    }

    public void draw() {
        double xMin, xMax, yMin, yMax;
        xMin = 0.0;
        yMin = 0.0;
        xMax = 1.0;
        yMax = 1.0;
        KdTreeNode prev = null;
        for (KdTreeNode k : iter()) {
            StdOut.printf("Drawing (%f, %f)\n", k.xVal, k.yVal);
            StdOut.printf("Current orientation is ");
            // Current point we're plotting has vertical orientation
            // previous point has horizontal orientation
            // we compare by y coordinate
            if (k.orientation == VERT) {
                StdOut.printf("vertical.\n");
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.RED);
                if (prev != null) {
                    int comp = prev.compareTo(k);
                    // we're lower than the previous horizontal line
                    if (comp > 0)
                        StdDraw.line(k.xVal, yMin, k.xVal, prev.yVal);
                    // we're higher than previous horizontal line
                    else if (comp < 0)
                        StdDraw.line(k.xVal, prev.yVal, k.xVal, yMax);
                }
                else
                    StdDraw.line(k.xVal, yMin, k.xVal, yMax);
            }
            // Current point we're plotting has horizontal orientation
            // previous point has vertical orientation
            // we compare by x coordinate
            else if (k.orientation == HORIZ) {
                StdOut.printf("horizontal.\n");
                int comp = prev.compareTo(k);
                StdDraw.setPenRadius(0.005);
                StdDraw.setPenColor(StdDraw.BLUE);
                // we're left of the previous vertical line
                if (comp > 0)
                    StdDraw.line(xMin, k.yVal, prev.xVal, k.yVal);
                // we're right of the previous vertical line
                else if (comp < 0)
                    StdDraw.line(prev.xVal, k.yVal, xMax, k.yVal);
            }
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(k.xVal, k.yVal);
            prev = k;
        }
    }

    // public Point2D nearest(Point2D p) {

    // }

    // public Iterable<Point2D> range(RectHV rect) {
    //     ArrayList<Point2D> a = new ArrayList<Point2D>();

    // }

    public static void main(String[] args) {
        KdTree tree  = new KdTree();
        for (int i = 0; i < 3; i++) {
            double rand1 = StdRandom.uniform();
            double rand2 = StdRandom.uniform();
            Point2D p = new Point2D(rand1, rand2);
            StdOut.println(p);
            tree.insert(p);
        }
        StdDraw.setPenRadius(0.01);
        StdOut.println("---------------");
        tree.draw();

        // set.draw();
        // double rand1 = scale*StdRandom.uniform();
        // double rand2 = scale*StdRandom.uniform();
        // Point2D m = new Point2D(rand1, rand2);
        // Point2D n = set.nearest(m);
        // RectHV r = new RectHV(20, 20, 100, 100);
        // r.draw();
        // Iterable<Point2D> it = set.range(r);
        // int count = 0;
        // for (Point2D q : it) {
        //     StdOut.println(count);
        //     StdOut.println(q);
        //     count++;
        // }
        //StdOut.println("--------------");
        //StdOut.println(m);
        //StdOut.println(n);
        //StdOut.println(m.distanceTo(n));
    }


}