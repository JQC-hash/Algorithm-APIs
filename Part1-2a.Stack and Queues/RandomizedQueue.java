/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        s = (Item[]) new Object[1];
        s[0] = null;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (size == s.length) {
            resize(2 * s.length);
        }
        s[size] = item;
        size++;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int counter = 0;
        int it = 0;
        while (counter < size) {
            if (s[it] != null) {
                copy[counter] = s[it];
                counter++;
            }
            it++;
        }
        s = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int rnd = StdRandom.uniform(0, size); // pick a random number from [0,n-1].
        Item removedItem = s[rnd];

        s[rnd] = s[size - 1];
        s[size - 1] = null;
        size--;

        if (size > 0 && size == s.length / 4) {
            resize(s.length / 2);
        }
        return removedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        int rnd;
        do {
            rnd = StdRandom.uniform(size);
        } while (s[rnd] == null);

        return s[rnd];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }


    private class ArrayIterator implements Iterator<Item> {
        private int index;
        private final Item[] shuffle;

        public ArrayIterator() {
            // copy existing items to the array named random.
            shuffle = (Item[]) new Object[size];
            int counter = 0;
            int it = 0;
            while (counter < size) {
                if (s[it] != null) {
                    shuffle[counter] = s[it];
                    counter++;
                }
                it++;
            }

            // shuffle the array twice. Single shuffle would fail randomness test.
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < size; j++) {
                    int position = StdRandom.uniform(size);
                    Item temp = shuffle[position];
                    shuffle[position] = shuffle[j];
                    shuffle[j] = temp;
                }
            }

            index = -1;
        }

        public boolean hasNext() {

            return index < size - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException("Method not supported.");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return shuffle[++index];
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rQueue.enqueue(item);
        }

        for (String s : rQueue) {
            StdOut.println(s);
        }
        StdOut.println("size of randomized queue = " + rQueue.size());

    }
}
