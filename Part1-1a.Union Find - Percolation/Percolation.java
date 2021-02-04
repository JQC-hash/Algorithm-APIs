/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private final WeightedQuickUnionUF unionFindA;
    private final WeightedQuickUnionUF unionFindB;
    private boolean[][] sites;
    private int openSiteCounter;

    // Constructor, create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        //
        if (n <= 0)
            throw new IllegalArgumentException("Input n must be a positive integer.");
        else {
            sites = new boolean[n][n];
            // initialization
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    sites[i][j] = false; // false=blocked, true=open
                }
            }
        }
        openSiteCounter = 0;

        unionFindA = new WeightedQuickUnionUF(n * n + 1);
        unionFindB = new WeightedQuickUnionUF(n * n + 2);
        // n*n in the array is the top virtual site, n*n+1 is the bottom virtual site.
        // }

        // catch (IllegalArguementException e) {
        //   StdOut.println("Input must be a positive integer.");
        //}

    }

    private int convert(int row, int col) {
        return (row - 1) * sites.length + (col - 1);
    }

    private boolean validate(int row, int col) {
        return (row >= 1 && row <= sites.length && col >= 1 && col <= sites.length && isOpen(row,
                                                                                             col));
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > sites.length || col < 1 || col > sites.length)
            throw new IllegalArgumentException(" Open grid is outside the prescribed range.");
        else {
            if (!isOpen(row, col)) {

                sites[row - 1][col - 1] = true; // open-up
                openSiteCounter++;

                int currentIndex = convert(row, col); // the position of current opened-up grid
                if (row == 1) {
                    unionFindA.union(currentIndex, sites.length * sites.length);
                    unionFindB.union(currentIndex, sites.length * sites.length);
                    // if the opened site is at top, connect it with the top virtual site
                }
                if (row == sites.length) {
                    unionFindB.union(currentIndex, sites.length * sites.length + 1);
                    // if the opened site is at bottom, connect it with the bottom virtual site
                }

                if (validate(row - 1, col)) {
                    int sideIndex = convert(row - 1, col);
                    unionFindA.union(currentIndex, sideIndex);
                    unionFindB.union(currentIndex, sideIndex);
                }
                if (validate(row + 1, col)) {
                    int sideIndex = convert(row + 1, col);
                    unionFindA.union(currentIndex, sideIndex);
                    unionFindB.union(currentIndex, sideIndex);
                }
                if (validate(row, col - 1)) {
                    int sideIndex = convert(row, col - 1);
                    unionFindA.union(currentIndex, sideIndex);
                    unionFindB.union(currentIndex, sideIndex);
                }
                if (validate(row, col + 1)) {
                    int sideIndex = convert(row, col + 1);
                    unionFindA.union(currentIndex, sideIndex);
                    unionFindB.union(currentIndex, sideIndex);
                }
            }
        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > sites.length || col < 1 || col > sites.length)
            throw new IllegalArgumentException("isOpen grid is outside the prescribed range.");
        else {
            return sites[row - 1][col - 1];
        }

    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > sites.length || col < 1 || col > sites.length)
            throw new IllegalArgumentException("isFull grid is outside the prescribed range.");
        else {
            int currentIndex = convert(row, col);
            // the position of the grid.
            return (unionFindA.connected(currentIndex, sites.length * sites.length) && unionFindB
                    .connected(currentIndex, sites.length * sites.length));
        }
    }


    // number of open sites
    public int numberOfOpenSites() {
        return openSiteCounter;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFindB.connected(sites.length * sites.length, sites.length * sites.length + 1);

    }

    // test client (optional)
    /* public static void main(String[] args) {
        StdOut.print("Input n and T:");
        int dimension = StdIn.readInt();
        int trials = StdIn.readInt();
        Percolation perc = new Percolation(dimension);
    }*/
}
