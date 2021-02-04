/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    private static final double CONFIDENCE_CONSTANT = 1.96;
    // private double[] test;
    // private int trials;

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;


    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Input must be 2 positive integers!");
        // this.trials = trials;
        double[] test = new double[trials];
        for (int i = 0; i < trials; i++) {
            test[i] = 0;
        }

        for (int k = 0; k < trials; k++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row;
                int col;
                do {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                }
                while (perc.isOpen(row, col));
                perc.open(row, col);
            }
            test[k] = (double) perc.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(test);
        stddev = StdStats.stddev(test);
        confidenceLo = mean - CONFIDENCE_CONSTANT * stddev / (Math.sqrt((double) trials));
        confidenceHi = mean + CONFIDENCE_CONSTANT * stddev / (Math.sqrt((double) trials));
    }


    // sample mean of percolation threshold
    public double mean() {

        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {

        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        // double confidenceLo = mean - CONFIDENCE_CONSTANT * stddev / (Math.sqrt((double) trials));
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // double confidenceHi = mean + CONFIDENCE_CONSTANT * stddev / (Math.sqrt((double) trials));
        return confidenceHi;
    }


    public static void main(String[] args) {
        Out out = new Out();
        In in = new In(args[0]);      // input file


        out.print("Input n and T:");
        String input = in.readLine();
        String delims = " ";
        String[] tokens = input.split(delims);
        if (tokens.length != 2)
            throw new IllegalArgumentException("Input must be 2 positive integers!");
        int dimension = Integer.parseInt(tokens[0]);
        int t = Integer.parseInt(tokens[1]);


        if (tokens[0] == null || tokens[1] == null || t <= 0 || dimension <= 0)
            throw new IllegalArgumentException("Input must be 2 positive integers!");

        PercolationStats stats = new PercolationStats(dimension, t);


        out.printf("\n%25s : " + stats.mean() + "\n", "mean");
        out.printf("\n%25s : " + stats.stddev() + "\n", "stddev");
        out.printf(
                "\n%25s : " + "[" + stats.confidenceLo() + "]" + "[" + stats.confidenceHi()
                        + "]"
                        + "\n",
                "95% confidence interval");

    }
}


