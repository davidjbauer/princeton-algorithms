// public class FastCollinearPoints {
//    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
//    public           int numberOfSegments()        // the number of line segments
//    public LineSegment[] segments()                // the line segments
// }
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    
    private int numSegs;
    final private LineSegment[] segs;

    public FastCollinearPoints(Point[] points) {
        
        List<Point> pointList;
        Point origin, tmpPoint;
        final Point[] inputPoints;
        double currentSlope;
        List<LineSegment> lineList = new ArrayList<LineSegment>();

        if (points == null) throw new IllegalArgumentException();

        this.numSegs = 0;
        Arrays.sort(points);
        inputPoints = points.clone();
        for (int i = 0; i < points.length; i++) {
            points = inputPoints.clone();
            origin = inputPoints[i];
            tmpPoint = inputPoints[0];
            points[0] = origin;
            points[i] = tmpPoint;

            if (origin == null) throw new IllegalArgumentException();

            Arrays.sort(points, 1, points.length, origin.slopeOrder());

            int k = 1;
            while (k < points.length) {
                if(points[k] == null) throw new IllegalArgumentException();
                currentSlope = origin.slopeTo(points[k]);
                pointList = new ArrayList<Point>();
                pointList.add(origin);
                pointList.add(points[k]);
                int numCollinear = 1;
                for (int h = k + 1; h < points.length; h++) {
                    if (Double.compare(currentSlope, origin.slopeTo(points[h])) == 0.0) {
                        pointList.add(points[h]);
                        numCollinear++;
                    }
                    else break;
                }
                Collections.sort(pointList);
                Point startPoint = pointList.get(0);
                if (numCollinear >= 3) {
                    if (startPoint.compareTo(origin) == 0) { 
                        Point endPoint = pointList.get(pointList.size() - 1);
                        lineList.add(new LineSegment(startPoint, endPoint));
                        numSegs++;
                    }
                    k = k + numCollinear;
                }
                else k++;
            }
        }
        segs = lineList.toArray(new LineSegment[0]);     
    }

    public int numberOfSegments() {
        return numSegs;
    }

    public LineSegment[] segments() {
        return segs.clone();
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