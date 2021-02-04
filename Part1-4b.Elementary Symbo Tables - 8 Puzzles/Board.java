/* *****************************************************************************
 *  Name: Jia-Qi Chen
 *  Date:28 Jan 2019
 *  Description: the 8-Puzzle assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public final class Board {
    private final int[][] data;
    private final int dimension;
    private final int hamming;
    private final int manhattan;
    // private ArrayList<Board> neighbors;

    public Board(int[][] blocks) {

        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) {
            throw new IllegalArgumentException();
        }
        dimension = blocks.length;
        if (dimension < 2) {
            throw new NullPointerException();
        }

        // calculate hamming
        int disPlaced = 0;
        int m = 0;

        // copy the board array and calculate at the same time
        data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = blocks[i][j];

                // calculate hamming
                if (data[i][j] != 0 && data[i][j] != (i * dimension + j + 1)) {
                    disPlaced = disPlaced + 1;
                }

                // calculate manhattan
                int col;
                int row;
                if (data[i][j] != 0) {
                    col = (data[i][j] - 1) % dimension;
                    row = (data[i][j] - 1 - col) / dimension;
                    m = m + Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        hamming = disPlaced;
        manhattan = m;
    }


    public int dimension() {
        // board dimension n
        return dimension;
    }

    public int hamming() {
        // number of blocks out of place
        return hamming;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        return manhattan;
    }

    public boolean isGoal() {
        // is this board the goal board?
        return (hamming == 0);
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = data[i][j];
            }
        }
        if (copy[0][0] != 0 && copy[0][1] != 0) {
            int temp = copy[0][0];
            copy[0][0] = copy[0][1];
            copy[0][1] = temp;
        }
        else {
            int temp = copy[1][0];
            copy[1][0] = copy[1][1];
            copy[1][1] = temp;
        }
        Board twin = new Board(copy);
        return twin;
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (this == y) {
            // same reference
            return true;
        }

        else if (y == null || y.getClass() != this.getClass()) {
            return false;
        }
        else {
            Board check = (Board) y;
            if (this.dimension != check.dimension) return false;
            if (this.hamming() != ((Board) y).hamming()) return false;
            if (this.manhattan() != ((Board) y).manhattan()) return false;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (this.data[i][j] != check.data[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }


    public Iterable<Board> neighbors() {
        // all neighboring boards
        // list all the neighbor boards
        // find the 0 in the data array
        int p = 0;
        int q = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (data[i][j] == 0) {
                    p = i;
                    q = j;
                    break;
                }
            }
        }

        // find all the swaps
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = data[i][j];
            }
        }

        Board swapUp;
        Board swapDown;
        Board swapLeft;
        Board swapRight;
        // ManhattanCompare manhattanCompare = new ManhattanCompare();
        // MinPQ<Board> neighbors = new MinPQ<Board>(manhattanCompare);
        ArrayList<Board> neighbors = new ArrayList<Board>();
        if (p != 0) {
            // swap with the upper block
            copy[p][q] = copy[p - 1][q];
            copy[p - 1][q] = 0;
            swapUp = new Board(copy);
            neighbors.add(swapUp);
            // restore the array
            copy[p - 1][q] = copy[p][q];
            copy[p][q] = 0;
        }


        if (p != dimension - 1) {
            // swap with the lower block
            copy[p][q] = copy[p + 1][q];
            copy[p + 1][q] = 0;
            swapDown = new Board(copy);
            neighbors.add(swapDown);
            copy[p + 1][q] = copy[p][q];
            copy[p][q] = 0;
        }


        if (q != 0) {
            // swap with the left block
            copy[p][q] = copy[p][q - 1];
            copy[p][q - 1] = 0;
            swapLeft = new Board(copy);
            neighbors.add(swapLeft);
            copy[p][q - 1] = copy[p][q];
            copy[p][q] = 0;
        }


        if (q != dimension - 1) {
            // swap with the right block
            copy[p][q] = copy[p][q + 1];
            copy[p][q + 1] = 0;
            swapRight = new Board(copy);
            neighbors.add(swapRight);
            copy[p][q + 1] = copy[p][q];
            copy[p][q] = 0;
        }
        return neighbors;
    }

    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder builder = new StringBuilder();
        builder.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                builder.append(" " + data[i][j]);
            }
            builder.append("\n");
        }
        String s = builder.toString();
        // StdOut.print(s);
        return s;
    }

    /* class ManhattanCompare implements Comparator<Board> {
        public int compare(Board a, Board b) {
            return Integer.compare(a.manhattan(), b.manhattan());
        }
    } */

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println("dimension = " + initial.dimension());
        StdOut.println("hamming = " + initial.hamming());
        StdOut.println("manhattan = " + initial.manhattan());

        Board twin = initial.twin();
        StdOut.println(twin);
    } // unit tests (not graded)
}





