/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // initialization
        numberOfSegments = 0;
        // segments = new LineSegment[0];
        segments = new LineSegment[points.length];

        // auxiliary array for slope order sorting
        Point[] aux = new Point[points.length];
        // storage array to store natural order sorting
        Point[] storage = new Point[points.length];
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
                storage[i] = points[i];
            }
        }

        // natural order
        Arrays.sort(aux);
        Arrays.sort(storage);

        for (int i = 0; i < points.length; i++) {
            Arrays.sort(aux, storage[i].slopeOrder());

            Point temp = aux[1];
            int pInLine = 2;
            for (int j = 1; j < aux.length - 1; j++) {
                if (aux[j].slopeTo(aux[0]) == aux[j + 1].slopeTo(aux[0])) {
                    pInLine++;
                    if (j + 1 == points.length - 1 && pInLine >= 4 && aux[0].compareTo(temp) <= 0) {
                        // segments = resize(numberOfSegments + 1, segments);
                        // segments[numberOfSegments] = new LineSegment(aux[0], aux[j + 1]);
                        segments[numberOfSegments] = new LineSegment(aux[0], aux[j + 1]);
                        numberOfSegments++;

                        pInLine = 2;
                        temp = aux[j + 1];
                    }
                }
                else { // if (!aux[j].equals(aux[j + 1]))
                    if (pInLine >= 4) {
                        // only count the segment when the pivot point is at the lower end
                        if (aux[0].compareTo(temp) <= 0) {
                            // segments = resize(numberOfSegments + 1, segments);
                            // segments[numberOfSegments] = new LineSegment(aux[0], aux[j]);
                            segments[numberOfSegments] = new LineSegment(aux[0], aux[j]);
                            numberOfSegments++;
                        }
                    }
                    pInLine = 2;
                    temp = aux[j + 1];
                }
            }
            // restore the natural order of the aux arrage
            for (int k = 0; k < storage.length; k++) {
                aux[k] = storage[k];
            }
        }
        segments = resize(numberOfSegments, segments);
    }

    private LineSegment[] resize(int capacity, LineSegment[] s) {
        LineSegment[] copy = new LineSegment[capacity];
        if (s.length != 0) {
            for (int z = 0; z < capacity; z++) {
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
        StdDraw.setYscale(0, 32678);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
