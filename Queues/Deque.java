import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Algorithms Week 2 Project -- Randomized Deques and Queues
 *
 * This program is a generic data type for a deque (double-ended queue).
 *
 * @author Amy Kang
 *
 * @version November 19, 2021
 */
public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int n;  // size of list

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        if (n == 0) {
            last = first;
        } else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.isEmpty()) {
            addFirst(item);
        } else {
            Node<Item> oldLast = last;
            last = new Node<>();
            last.item = item;
            last.previous = oldLast;
            last.previous.next = last;
            n++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        n--;
        if (!isEmpty()) {
            first.previous = null;
        } else {
            last = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (n == 1) {
            return removeFirst();
        }
        Item item = last.item;
        last = last.previous;
        last.next = null;
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void print() {
        Node<Item> current = first;
        while (current != null) {
            System.out.print(current.item + " ");
            current = current.next;
        }
        System.out.println();
    }


    // unit testing (required)
    public static void main(String[] args) {

        Deque<Integer> integerDeque = new Deque<>();
        integerDeque.addFirst(0);
        integerDeque.removeLast();
        integerDeque.addLast(1);
        integerDeque.addLast(2);
        integerDeque.addLast(3);
        integerDeque.addLast(4);
        integerDeque.addLast(5);

        StdOut.print("deque: ");
        integerDeque.print();
        StdOut.println("size: " + integerDeque.size());
        StdOut.println("deque is empty: " + integerDeque.isEmpty());

        StdOut.println("\nremoved first element: " + integerDeque.removeFirst());
        StdOut.println("removed first element: " + integerDeque.removeFirst());
        StdOut.print("deque: ");
        integerDeque.print();
        StdOut.println("size: " + integerDeque.size());

        StdOut.println("\nremoved last element: " + integerDeque.removeLast());
        StdOut.println("removed last element: " + integerDeque.removeLast());
        StdOut.println("removed last element: " + integerDeque.removeLast());
        StdOut.print("deque: ");
        integerDeque.print();
        StdOut.println("size: " + integerDeque.size());

        StdOut.println("\nadded first elements:  -2   -3   -4");
        integerDeque.addFirst(-2);
        integerDeque.addFirst(-3);
        integerDeque.addFirst(-4);
        StdOut.print("deque: ");
        integerDeque.print();

        StdOut.println("\nremoved last element: " + integerDeque.removeLast());
        StdOut.println("removed last element: " + integerDeque.removeLast());
        StdOut.print("deque: ");
        integerDeque.print();
        StdOut.println("size: " + integerDeque.size());

        StdOut.println("\nremoved: " + integerDeque.removeLast());

        // removeFirst causes loitering because last is still assigned to a value when the last item is deleted

    }

}
