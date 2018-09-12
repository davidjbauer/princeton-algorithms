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

    private class Segment implements Comparable<Segment> {
        public double slope;
        public Point end, start;

        public Segment(double slope, Point start, Point end) {
            this.slope = slope;
            this.end = end;
            this.start = start;
        }
        public int compareTo(Segment other) {
            return Double.compare(this.slope, other.slope);
        }
    }

    public FastCollinearPoints(Point[] points) {
        
        List<Point> pointList;
        Point origin, tmpPoint;
        Point[] sortPoints;
        double currentSlope;
        LineSegment tmpSegment;
        List<Segment> segmentList = new ArrayList<Segment>();
        List<LineSegment> lineList = new ArrayList<LineSegment>();

        if (points == null) throw new IllegalArgumentException();

        this.numSegs = 0;
        Arrays.sort(points);
        sortPoints = points.clone();
        for (int i = 0; i < points.length; i++) {
            points = sortPoints.clone();
            origin = sortPoints[i];
            tmpPoint = sortPoints[0];
            points[0] = origin;
            points[i] = tmpPoint;

            if (origin == null) throw new IllegalArgumentException();

            Arrays.sort(points, 1, points.length, origin.slopeOrder());

            int k = 1;
            while (k < points.length) {
                if(points[k] == null) throw new IllegalArgumentException();
                currentSlope = origin.slopeTo(points[k]);
                pointList = new ArrayList<Point>();
                pointList.add(points[k]);
                int numCollinear = 1;
                for (int h = k + 1; h < points.length; h++) {
                    if (Double.compare(currentSlope, origin.slopeTo(points[h])) == 0.0) {
                        pointList.add(points[h]);
                        numCollinear++;
                    }
                    else break;
                }
                if (numCollinear >= 3) {
                    Collections.sort(pointList);
                    Point startPoint = pointList.get(0);
                    Point endPoint = pointList.get(pointList.size() - 1);
                    segmentList.add(new Segment(currentSlope, startPoint, endPoint));
                    numSegs++;
                    k = k + numCollinear;
                }
                else k++;
            }
        }
        Collections.sort(segmentList);
        for (int j = 0; j < segmentList.size()-1; j++) {
            Segment current = segmentList.get(j);
            Segment next = segmentList.get(j+1);
            if (current.compareTo(next) == 0) {
                if (current.end.compareTo(next.end) >= 0)
                    lineList.add(new LineSegment(next.start, next.end));
                else {
                    lineList.add(new LineSegment(current.start, current.end));
                    ++j;
                }
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