import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

    
// API Specification 
// public class PointSET {
// +   public         PointSET()                               // construct an empty set of points 
//  +  public           boolean isEmpty()                      // is the set empty? 
//  +  public               int size()                         // number of points in the set 
//  +  public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
//    public           boolean contains(Point2D p)            // does the set contain point p? 
//  +  public              void draw()                         // draw all points to standard draw 
//  +  public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary) 
//  +  public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 

//    public static void main(String[] args)                  // unit testing of the methods (optional) 
// }

public class PointSET {
    private SET<Point2D> points;
    public PointSET() {
        this.points = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size() {
        return this.points.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        this.points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return this.points.contains(p);
        
    }

    public void draw() {
        for (Point2D p : this.points) {
            p.draw();
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Point2D neighbor = null;
        double minDist = Double.POSITIVE_INFINITY;
        for (Point2D q : this.points) {
            if (p.distanceTo(q) < minDist) { 
                neighbor = q;
                minDist = p.distanceTo(q);
            }
        }
        return neighbor;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        for (Point2D q : this.points) {
            if (rect.contains(q))
                a.add(q);
        }
        return (Iterable<Point2D>)a;
    }

    public static void main(String[] args) {
        // PointSET set = new PointSET();
        // double scale = 200;
        // for (int i = 0; i < 100; i++) {
        //     double rand1 = scale*StdRandom.uniform();
        //     double rand2 = scale*StdRandom.uniform();
        //     Point2D p = new Point2D(rand1, rand2);
        //     //StdOut.println(p);
        //     set.insert(p);
        // }
        // StdDraw.setPenRadius(0.01);
        // StdDraw.setXscale(0, scale);
        // StdDraw.setYscale(0, scale);
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