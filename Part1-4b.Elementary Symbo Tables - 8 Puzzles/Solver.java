/* *****************************************************************************
 *  Name: Jia-Qi Chen
 *  Date: 28 Jan 2019
 *  Description: the 8-Puzzle assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public final class Solver {

    private SearchNode originLast;
    private boolean isSolvable;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        isSolvable = true;

        MinPQ<SearchNode> queue = new MinPQ<SearchNode>();
        queue.insert(new SearchNode(initial, null, 0, false));
        queue.insert(new SearchNode(initial.twin(), null, 0, true));

        while (!queue.isEmpty()) {
            SearchNode processed = queue.delMin();
            if (!processed.isTwin) {
                originLast = processed;
            }
            if (processed.board.isGoal()) {
                if (processed.isTwin) {
                    isSolvable = false;
                }
                break;
            }

            for (Board neighbor : processed.board.neighbors()) {
                if (processed.previous == null || !processed.previous.board.equals(neighbor)) {
                    queue.insert(new SearchNode(neighbor, processed, processed.moves + 1,
                                                processed.isTwin));
                }
            }
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        else return originLast.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<Board>();
        SearchNode current = originLast;

        while (current.previous != null) {
            solution.push(current.board);
            current = current.previous;
        }
        solution.push(current.board);

        return solution;
    }


    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int moves;
        private boolean isTwin;

        // constructor
        public SearchNode(Board board, SearchNode previous, int moves,
                          boolean isTwin) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
            this.isTwin = isTwin;
        }

        public int compareTo(SearchNode that) {
            int thisPriority = this.board.manhattan() + this.moves;
            int thatPriority = that.board.manhattan() + that.moves;

            if (thisPriority > thatPriority) return 1;
            else if (thisPriority < thatPriority) return -1;
            else return 0;
        }
    }

    public static void main(String[] args) { // solve a slider puzzle (given below)

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible, moves =" + solver.moves());
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
