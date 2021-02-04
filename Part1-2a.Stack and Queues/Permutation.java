/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        /* String in = args[0];
        String delim = " < ";
        String[] tokens = input.split(delim);
        if (tokens.length != 2)
            throw new IllegalArgumentException("Input must contain 2 components.");

        int printOut = Integer.parseInt(tokens[0]);
        In in = new In(tokens[1]);      // input file*/

        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();
        int printOut = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            /* String line = StdIn.readLine();
            String regex = " ";
            tokens = line.split(regex);

            for (int j = 0; j < tokens.length; j++) {
                rQueue.enqueue(tokens[j]);
            }*/
            String word = StdIn.readString();
            rQueue.enqueue(word);
        }

        Iterator<String> it = rQueue.iterator();
        for (int i = 0; i < printOut; i++) {
            StdOut.println(it.next());
        }


    }
}
