/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;


public class BruteCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        numberOfSegments = 0;
        segments = new LineSegment[0];

        // create an auxiliary array and copy from points array
        Point[] aux = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            else {
                for (int j = 0; j < i; j++) {
                    if (points[i].equals(aux[j])) {
                        throw new IllegalArgumentException();
                    }
                }
                aux[i] = points[i];
            }
        }
        Arrays.sort(aux);
        // finds all line segments containing 4 points
        for (int i = 0; i < aux.length - 3; i++) {
            for (int j = i + 1; j < aux.length - 2; j++) {
                for (int p = j + 1; p < aux.length - 1; p++) {
                    for (int q = p + 1; q < aux.length; q++) {
                        if (aux[j].slopeTo(aux[i]) == aux[p].slopeTo(aux[i])
                                && aux[j].slopeTo(aux[i]) == aux[q].slopeTo(aux[i])) {
                            segments = resize(numberOfSegments + 1, segments);
                            segments[numberOfSegments] = new LineSegment(aux[i], aux[q]);
                            numberOfSegments++;
                        }
                    }
                }
            }
        }
    }

    private LineSegment[] resize(int capacity, LineSegment[] s) {
        LineSegment[] copy = new LineSegment[capacity];
        if (s.length != 0) {
            for (int z = 0; z < s.length; z++) {
                copy[z] = s[z];
            }
        }
        else copy[0] = null;

        return copy;
    }


    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] copy = new LineSegment[segments.length];
        for (int i = 0; i < segments.length; i++) {
            copy[i] = segments[i];
        }
        return copy;
    }

    public static void main(String[] args) {
        // read input from file and draw points
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
