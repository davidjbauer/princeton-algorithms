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
    private static final boolean VERT = false;
    private static final boolean HORIZ = true;
    private class KdTreeNode implements Comparable<KdTreeNode>  {
        public boolean orientation;
        public Point2D pt;
        public KdTreeNode left, right;
        public RectHV rect;
        public KdTreeNode(Point2D p, RectHV r, boolean o) {
            this.left = this.right = null;
            this.pt = p;
            this.orientation = o;
            this.rect = r;
        }
        public int compareTo(KdTreeNode that) {
            // first node is vertically oriented
            // compare by x coordinate
            if (this.orientation == VERT) {
                if (this.pt.x() < that.pt.x())
                    return -1;
                else if (this.pt.x() > that.pt.x())
                    return 1;
                else return 0;
            }
            // first node horizontally oriented,
            // compare by y coordinate
            else {
                if (this.pt.y() < that.pt.y())
                    return -1;
                else if (this.pt.y() > that.pt.y())
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
    private KdTreeNode insert(KdTreeNode orig, Point2D insertPt, RectHV currRect, boolean currOrient) {
        RectHV newRect = currRect;
        KdTreeNode curr = orig;
        KdTreeNode toAdd = new KdTreeNode(insertPt, currRect, currOrient);
        if (curr == null) {

            ++this.size;
            return toAdd;
        }
        int comp = curr.compareTo(toAdd);

        if (comp < 0) {
            if (currOrient == VERT)
                newRect = new RectHV(curr.pt.x(), currRect.ymin(), currRect.xmax(), currRect.ymax());
            else if (currOrient == HORIZ)
                newRect = new RectHV(currRect.xmin(), curr.pt.y(), currRect.xmax(), currRect.ymax());
            curr.left = insert(curr.left, insertPt, newRect, !currOrient);
        }
        else if (comp >= 0) {
            if (currOrient == VERT)
                newRect = new RectHV(currRect.xmin(), currRect.ymin(), curr.pt.x(), currRect.ymax());
            else if (currOrient == HORIZ)
                newRect = new RectHV(currRect.xmin(), currRect.ymin(), currRect.xmax(), curr.pt.y());
            curr.right = insert(curr.right, insertPt, newRect, !currOrient);
        }
        return curr;
    }

    public void insert(Point2D p) {
        this.root = insert(this.root, p, new RectHV(0, 0 , 1, 1), VERT);
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

    public boolean contains(Point2D p) {
        Point2D q = nearest(p);
        return q.equals(p);
    }


    public void draw() {
        for (KdTreeNode k : iter()) {
            StdDraw.setPenRadius(0.005);
            // Current point we're plotting has vertical orientation
            // previous point has horizontal orientation
            // we compare by y coordinate
            if (k.orientation == VERT) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(k.pt.x(), k.rect.ymin(), k.pt.x(), k.rect.ymax());
            }
            // Current point we're plotting has horizontal orientation
            // previous point has vertical orientation
            // we compare by x coordinate
            else if (k.orientation == HORIZ) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(k.rect.xmin(), k.pt.y(), k.rect.xmax(), k.pt.y());
            }
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(k.pt.x(), k.pt.y());
        }
    }

    public Point2D nearest(Point2D p) {
        return nearest(this.root, p, null);
    }

    private Point2D nearest(KdTreeNode orig, Point2D p, Point2D prevClosest) {
        Point2D closest = prevClosest;
        if (orig == null)
            return closest;
        if (prevClosest == null)
            closest = orig.pt;
        double prevDist = p.distanceTo(closest);
        double currDist = p.distanceTo(orig.pt);
        if (currDist < prevDist)
            closest = orig.pt;
        if (orig.left != null && orig.left.rect.distanceTo(p) < currDist)
            closest = nearest(orig.left, p, closest);
        if (orig.right != null && orig.right.rect.distanceTo(p) < currDist)
            closest = nearest(orig.right, p, closest);
        return closest;
    }


    public Iterable<Point2D> range(RectHV rect) {
        return range(root, rect, new ArrayList<Point2D>());
    }

    private Iterable<Point2D> range(KdTreeNode orig, RectHV rect, ArrayList<Point2D> array) {
        if (orig == null) return array;
        if (rect.contains(orig.pt)) 
            array.add(orig.pt);
        if (orig.left != null && rect.intersects(orig.left.rect))
            range(orig.left, rect, array);
        if (orig.right != null && rect.intersects(orig.right.rect))
            range(orig.right, rect, array);
        return array;
    }

    public static void main(String[] args) {
        KdTree tree  = new KdTree();
        for (int i = 0; i < 20; i++) {
            double rand1 = StdRandom.uniform();
            double rand2 = StdRandom.uniform();
            Point2D p = new Point2D(rand1, rand2);
            StdOut.println(p);
            tree.insert(p);
        }

        tree.draw();
        double rand1 = StdRandom.uniform();
        double rand2 = StdRandom.uniform();
        Point2D p = new Point2D(rand1, rand2);
        Point2D q = tree.nearest(p);
        tree.draw();
        StdOut.println(tree.contains(p));
        StdOut.println(tree.contains(q));
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(p.x(), p.y());
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.point(q.x(), q.y());

    }


}