/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }


    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (first == null);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (isEmpty()) {
            first = new Node<Item>();
            first.item = item;
            first.next = null;
            first.previous = null;
            last = first;
            size = size + 1;
        }

        else {
            Node<Item> oldFirst = first;
            first = new Node<Item>();
            first.item = item;
            first.next = oldFirst;
            first.previous = null;
            oldFirst.previous = first;
            size = size + 1;
        }

    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (isEmpty()) {
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            last.previous = null;
            first = last;
            size = size + 1;
        }
        else {
            Node<Item> oldLast = last;
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            last.previous = oldLast;
            oldLast.next = last;
            size = size + 1;
        }

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item removedItem = first.item;
        if (size == 1) {
            first.item = null;
            first = null;
            last = null;
        }
        else {
            Node<Item> newFirst = first.next;
            first.item = null;
            first.previous = null;
            first.next = null;
            newFirst.previous = null;
            first = newFirst;
        }

        size = size - 1;
        return removedItem;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item removedItem = last.item;
        if (size == 1) {
            last.item = null;
            last = null;
            first = null;
        }
        else {
            Node<Item> newLast = last.previous;
            last.item = null;
            last.next = null;
            last.previous = null;
            newLast.next = null;
            last = newLast;
        }
        size = size - 1;
        return removedItem;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator(first);
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Method not supported.");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }


    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addFirst(item);
        }

        StdOut.println("size of deque = " + deque.size());
        for (String s : deque) {
            StdOut.println(s);
        }
    }
}
