/* *****************************************************************************
 *  Name: Jia-Qi Chen
 *  Date: 10 Feb 2019
 *  Description: PointSET is a nutable data type that represents a set of points
 *  in a unit square (1x1) using a red-black BST
 *  (algs4.jar\edu\princeton\cs\algs4\SET or java.util.TreeSet).
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;


public class PointSET {

    // a tree set sorted according to y then x coordinates
    private TreeSet<Point2D> original;


    public PointSET() {
        // construct an empty set of points
        original = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        // is the set empty
        return original.isEmpty();
    }

    public int size() {
        // number of points in the set
        return original.size();
    }

    public void insert(Point2D p) {
        // add point p into the set if it is not already in the set
        if (p == null) throw new IllegalArgumentException("Called insert method with a null input");
        original.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p
        if (p == null)
            throw new IllegalArgumentException("Called contains method with a null input");
        else return original.contains(p);
    }

    public void draw() {
        // draw all the points to standard draw
        // Iterator<Point2D> it = original.iterator();
        for (Point2D p : original) {
            StdDraw.point(p.x(), p.y());
        }
    }


    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside or on the boundary of the rectangle
        if (rect == null)
            throw new IllegalArgumentException("Called range method with a null input");
        Point2D lowerBound = original.ceiling(new Point2D(rect.xmin(), rect.ymin()));
        Point2D upperBound = original.floor(new Point2D(rect.xmax(), rect.ymax()));

        // when there are no bounds that fall on the boundary or inside the area, return null
        if (lowerBound == null || upperBound == null || lowerBound.y() - upperBound.y() > 0)
            return null;

        TreeSet<Point2D> subsetY = new TreeSet<Point2D>(Point2D.X_ORDER);
        try {
            subsetY.addAll((TreeSet) original.subSet(lowerBound, true, upperBound, true));
        }
        catch (NullPointerException e) {
            return null;
        }

        lowerBound = subsetY.ceiling(new Point2D(rect.xmin(), rect.ymin()));
        upperBound = subsetY.floor(new Point2D(rect.xmax(), rect.ymax()));

        // when there are no bounds that fall on the boundary or inside the area, return null*********
        if (lowerBound == null || upperBound == null || lowerBound.x() - upperBound.x() > 0)
            return null;

        TreeSet<Point2D> subsetX = new TreeSet<Point2D>();
        try {
            subsetX.addAll((TreeSet) subsetY.subSet(lowerBound, true, upperBound, true));
        }
        catch (NullPointerException e) {
            return null;
        }

        return subsetX;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null)
            throw new IllegalArgumentException("Called nearest method with a null input");

        TreeSet<Point2D> distanceTree = new TreeSet<Point2D>(p.distanceToOrder());
        distanceTree.addAll(original);
        return distanceTree.first();
    }


    public static void main(String[] args) {
        // unit testing of the methods
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);

            brute.insert(p);
        }


        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();
        

        // draw the rectangle
        // RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1), Math.max(x0, x1), Math.max(y0, y1));
        RectHV rect = new RectHV(0.5, 0.3, 0.6, 0.7);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rect.draw();

        // draw in red the nearest neighbor (using brute-force algorithm)
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.BLUE);
        brute.nearest(new Point2D(0.5, 0.5)).draw();


        // draw the range search results for brute-force data structure in red
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.RED);
        TreeSet<Point2D> range = (TreeSet) brute.range(rect);
        if (range != null) {
            for (Point2D p : range) {
                p.draw();
            }
        }


        StdDraw.show();
        StdDraw.pause(20);

    }
}

