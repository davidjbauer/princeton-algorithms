// public class BruteCollinearPoints {
//    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
//    public           int numberOfSegments()        // the number of line segments
//    public LineSegment[] segments()                // the line segments
// }
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {

    private class Node {
        public Node next;
        public LineSegment segment;
        public Node(LineSegment s) {
            segment = s;
            next = null;
        }
    }
    private Node head;
    private LineSegment[] mySegments;
    private int numSegs;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.IllegalArgumentException();
        int length = points.length;
        Point [] tmpPoints = new Point[4];
        Point [] sortPoints;
        double slope1, slope2, slope3;
        Node current = null;
        for (int i = 0; i < length; i++) {
            if (points[i] == null)
                throw new java.lang.IllegalArgumentException();
            tmpPoints[0] = points[i];

            for (int j = i + 1; j < length; j++) {
                tmpPoints[1] = points[j];
                slope1 = tmpPoints[0].slopeTo(tmpPoints[1]);
 
                if (tmpPoints[1] == null || slope1 == Double.NEGATIVE_INFINITY)
                    throw new java.lang.IllegalArgumentException();
                for (int k = j + 1; k < length; k++) {
                    tmpPoints[2] = points[k];
                    slope2 = tmpPoints[0].slopeTo(tmpPoints[2]);

                    if (tmpPoints[2] == null || slope2 == Double.NEGATIVE_INFINITY)
                        throw new java.lang.IllegalArgumentException();
                    if (slope1 == slope2)
                        for (int h = k + 1; h < length; h++) {
                            tmpPoints[3] = points[h];
                            slope3 = tmpPoints[0].slopeTo(tmpPoints[3]);

                            if (tmpPoints[3] == null || slope3 == Double.NEGATIVE_INFINITY)
                                throw new java.lang.IllegalArgumentException();
                            if (slope3 == slope2) {
                                sortPoints = Arrays.copyOf(tmpPoints, tmpPoints.length);
                                Arrays.sort(sortPoints);
                                if (head == null) {
                                    head = new Node(new LineSegment(sortPoints[0], sortPoints[3]));
                                    current = head;
                                }
                                else if (current != null) {
                                    current.next = new Node(new LineSegment(sortPoints[0], sortPoints[3]));
                                    current = current.next;
                                }
                                numSegs++;

                            }
                        }
                }
            }
        }

        current = head;
        mySegments = new LineSegment[numSegs];
        for(int i = 0; i < numSegs; i++) {
            mySegments[i] = current.segment;
            current = current.next;
        }

    }

    public int numberOfSegments() {
        return numSegs;
    }

    public LineSegment[] segments() {
        return mySegments;
    }

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

        //print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}