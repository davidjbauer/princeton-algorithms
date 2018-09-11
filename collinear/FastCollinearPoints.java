// public class FastCollinearPoints {
//    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
//    public           int numberOfSegments()        // the number of line segments
//    public LineSegment[] segments()                // the line segments
// }
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {
    
    private class Node<Item> {
        public Node<Item> next;
        public Item item;
        public Node(Item s) {
            item = s;
            next = null;
        }
    }

    private Node<Point> head, current;
    private Node<LineSegment> segHead, segCurr;
    private int numSegs;
    private LineSegment[] segs;
    private Point pt0, pt1;
    private Point[] sortPoints;
    private double tmpSlope;
    private int tmpIndex;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        numSegs = 0;
        // For each Point passed in the argument, sort the subarray to the right.
        // [p1, p2, .. pN]
        // [p1, sort([p2, .., pN])
        //   ^
        // [p1, pm2, sort([pm3, ... pmN])]
        //       ^
        for (int i = 0; i < points.length; i++) {
            pt0 = points[i];

            if (pt0 == null)
                throw new java.lang.IllegalArgumentException();

            Arrays.sort(points, i+1, points.length, pt0.slopeOrder());

            // Now loop through the points, which are sorted by their slope
            // to our original point. For each point in THIS loop,
            // check if its slope w.r.t. the original point is equal
            // to the subsequent point. If it's not, move on.
            // If it IS, then add it to a linked list.
            int k = i+1;
            while (k < points.length) {
                tmpSlope = pt0.slopeTo(points[i+1]);
                tmpIndex = k + 1;
                head = new Node<Point>(points[i+1]);
                current = head;
                for (int h = k + 1; h < points.length; h++) {
                    if (tmpSlope == pt0.slopeTo(points[h])) {
                        tmpIndex = h;
                        current.next = new Node<Point>(points[h]);
                        current = current.next;
                    }
                    else break;
                }
                // Now we take all the collinear points we found and put
                // them in an array. Then we sort the array to find the
                // first and last points in the sequence. Finally we 
                // create a line segment between the first and last points
                // and add that to a linked list.
                int numCollinear = tmpIndex - k + 1;
                StdOut.printf("numCollinear = %d\n", numCollinear);
                if (numCollinear > 3) {
                    sortPoints = new Point[numCollinear];
                    current = head;
                    for (int j = 0; j < numCollinear; j++) {
                        sortPoints[j] = current.item;
                        current = current.next;
                    }
                    Arrays.sort(sortPoints);
                    if(segHead == null) {
                        segHead = new Node<LineSegment>(new LineSegment(sortPoints[0], sortPoints[tmpIndex - k]));
                        segCurr = segHead;
                    }
                    else {
                        segCurr.next = new Node<LineSegment>(new LineSegment(sortPoints[0], sortPoints[tmpIndex - k]));
                        segCurr = segCurr.next;
                    }
                    numSegs++;
                }
                k = tmpIndex + 1;
            }
        }
        segCurr = segHead;
        segs = new LineSegment[numSegs];
        for(int j = 0; j < numSegs; j++) {
            segs[j] = segCurr.item;
            segCurr = segCurr.next;
        }

    }

    public int numberOfSegments() {
        return numSegs;
    }

    public LineSegment[] segments() {
        return segs;
    }

    // Sample client.
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }


}