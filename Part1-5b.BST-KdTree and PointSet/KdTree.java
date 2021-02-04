/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description: KdTree is a nutable data type that represents a set of points
 *  in a unit square (1x1) using a Kd BST.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private static final RectHV UNIT = new RectHV(0, 0, 1, 1);
    private int size;
    private Node root;

    private static class Node {
        private double x;
        private double y;
        private Node left;
        private Node right;
        private boolean vertical;

        public Node(double x, double y, Node left, Node right, boolean vertical) {
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
            this.vertical = vertical;
        }
    }

    public KdTree() {
        // construct an empty tree of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the set empty
        return size == 0;
    }

    public int size() {
        // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        // add point p into the set if it is not already in the set
        if (p == null)
            throw new IllegalArgumentException("Called insert method with a null input.");
        root = insert(root, p, true);
    }

    private Node insert(Node node, Point2D p, boolean vertical) {
        // if it is a new node, creat it
        if (node == null) {
            size++;
            return new Node(p.x(), p.y(), null, null, vertical);
        }
        // if the point is already in the tree, return where it is found
        if (node.x == p.x() && node.y == p.y()) {
            return node;
        }

        // if the point is not in the tree, add it
        if ((node.vertical && p.x() < node.x) || (!node.vertical && p.y() < node.y)) {
            node.left = insert(node.left, p, !node.vertical);
        }
        else node.right = insert(node.right, p, !node.vertical);

        return node;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p
        if (p == null)
            throw new IllegalArgumentException("Called contains method with a null input.");
        return contains(root, p.x(), p.y());
    }

    private boolean contains(Node node, double x, double y) {
        if (node == null) return false;
        if (node.x == x && node.y == y) return true;
        if ((node.vertical && x < node.x) || (!node.vertical && y < node.y))
            return contains(node.left, x, y);
        else return contains(node.right, x, y);
    }

    public void draw() {
        // draw all the points to standard draw
        StdDraw.setScale(0, 1);
        StdDraw.setPenColor(Color.BLACK);
        // why?
        StdDraw.setPenRadius();
        UNIT.draw();

        draw(root, UNIT);
    }

    private void draw(Node node, RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Called draw method with null input.");

        // the last layer of node
        if (node == null) return;

        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        new Point2D(node.x, node.y).draw();

        // find out the 2 ends of division line
        Point2D min;
        Point2D max;

        if (node.vertical) {
            min = new Point2D(node.x, rect.ymin());
            max = new Point2D(node.x, rect.ymax());
        }
        else {
            min = new Point2D(rect.xmin(), node.y);
            max = new Point2D(rect.xmax(), node.y);
        }

        // draw the division line
        StdDraw.setPenRadius();
        min.drawTo(max);

        // draw next layer of nodes
        draw(node.left, leftRect(rect, node));
        draw(node.right, rightRect(rect, node));

    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside or on the boundary of the rectangle
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, UNIT, rect, q);
        return q;
    }

    private void range(Node node, RectHV container, RectHV searchingArea, Queue<Point2D> queue) {
        if (node == null) return;

        // only do range search if the searching area overlap the container
        if (container.intersects(searchingArea)) {
            Point2D p = new Point2D(node.x, node.y);
            if (searchingArea.contains(p)) queue.enqueue(p);
            if (node.vertical) {
                if (searchingArea.xmin() < p.x())
                    range(node.left, leftRect(container, node), searchingArea, queue);
                if (searchingArea.xmax() > p.x())
                    range(node.right, rightRect(container, node), searchingArea, queue);
            }
            else {
                if (searchingArea.ymin() < p.y())
                    range(node.left, leftRect(container, node), searchingArea, queue);
                if (searchingArea.ymax() > p.y())
                    range(node.right, rightRect(container, node), searchingArea, queue);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        return nearest(root, UNIT, p.x(), p.y(), null);
    }

    private Point2D nearest(Node node, RectHV rect, double x, double y, Point2D defendant) {
        // if it is an empty tree or when reaching the lowest layer
        if (node == null) return defendant;

        // if the node itself matches the query point, return the point in the node
        if (node.x == x && node.y == y) return new Point2D(node.x, node.y);

        Point2D nearest = defendant;
        Point2D query = new Point2D(x, y);
        double dqn = 0.0;
        double drq = 0.0;
        RectHV left = leftRect(rect, node);
        RectHV right = rightRect(rect, node);


        if (nearest != null) {
            dqn = query.distanceSquaredTo(nearest);
            drq = rect.distanceSquaredTo(query);
        }
        if (nearest == null || dqn > drq) {
            Point2D newPoint = new Point2D(node.x, node.y);
            if (nearest == null || query.distanceSquaredTo(newPoint) < dqn) nearest = newPoint;

            if (node.vertical) {
                // left = new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
                // right = new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
                if (x < node.x) {
                    nearest = nearest(node.left, left, x, y, nearest);
                    nearest = nearest(node.right, right, x, y, nearest);
                }
                else {
                    nearest = nearest(node.right, right, x, y, nearest);
                    nearest = nearest(node.left, left, x, y, nearest);
                }
            }
            else {
                // left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
                // right = new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
                if (y < node.y) {
                    nearest = nearest(node.left, left, x, y, nearest);
                    nearest = nearest(node.right, right, x, y, nearest);
                }
                else {
                    nearest = nearest(node.right, right, x, y, nearest);
                    nearest = nearest(node.left, left, x, y, nearest);
                }
            }
        }
        return nearest;
    }

    private RectHV leftRect(RectHV rect, Node node) {
        // divide the rectangle into left with the x or y coordinate of the point
        if (rect == null || node == null)
            throw new IllegalArgumentException("Called leftRect method with null input.");
        if (node.vertical) {
            return new RectHV(rect.xmin(), rect.ymin(), node.x, rect.ymax());
        }
        else return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.y);
    }

    private RectHV rightRect(RectHV rect, Node node) {
        if (rect == null || node == null)
            throw new IllegalArgumentException("Called rightRect method with null input.");
        if (node.vertical) {
            return new RectHV(node.x, rect.ymin(), rect.xmax(), rect.ymax());
        }
        else return new RectHV(rect.xmin(), node.y, rect.xmax(), rect.ymax());
    }


    public static void main(String[] args) {
        // unit testing of the methods
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdTree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);

            kdTree.insert(p);
        }


        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        kdTree.draw();
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
        kdTree.nearest(new Point2D(0.5, 0.5)).draw();


        // draw the range search results for brute-force data structure in red
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.RED);
        Queue<Point2D> range = (Queue) kdTree.range(rect);
        if (range != null) {
            for (Point2D p : range) {
                p.draw();
            }
        }


        StdDraw.show();
        StdDraw.pause(20);
    }
}
