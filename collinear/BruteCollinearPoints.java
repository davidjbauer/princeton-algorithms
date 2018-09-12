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
import java.util.ArrayList;

public class BruteCollinearPoints {

    private LineSegment[] mySegments;
    private int numSegs;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int length = points.length;
        
        Point [] sortPoints;
        ArrayList<LineSegment> segmentList = new ArrayList<LineSegment>();
        double slope1, slope2, slope3;
        for (int i = 0; i < length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            Point [] tmpPoints = new Point[4];
            tmpPoints[0] = points[i];
            
            for (int j = i + 1; j < length; j++) {
                if(points[j] == null) throw new IllegalArgumentException();
                tmpPoints[1] = points[j];
                slope1 = tmpPoints[0].slopeTo(tmpPoints[1]);
                if (slope1 == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
                
                for (int k = j + 1; k < length; k++) {
                    if(points[k] == null) throw new java.lang.IllegalArgumentException();
                    tmpPoints[2] = points[k];
                    slope2 = tmpPoints[0].slopeTo(tmpPoints[2]);
                    if (slope2 == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
                    if (Double.compare(slope1, slope2) == 0)
                        for (int h = k + 1; h < length; h++) {
                            if(points[h] == null) throw new IllegalArgumentException();
                            tmpPoints[3] = points[h];
                            slope3 = tmpPoints[0].slopeTo(tmpPoints[3]);

                            if (slope3 == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
                            if (Double.compare(slope2, slope3) == 0) {
                                sortPoints = tmpPoints.clone();
                                Arrays.sort(sortPoints);
                                segmentList.add(new LineSegment(sortPoints[0], sortPoints[3]));
                                numSegs++;
                                break;
                            }
                        }
                }
            }
        }

        mySegments = segmentList.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return numSegs;
    }

    public LineSegment[] segments() {
        return mySegments.clone();
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