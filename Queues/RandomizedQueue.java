import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Algorithms Week 2 Project -- Randomized Deques and Queues
 *
 * This program is a generic data type for a randomized queue, which removes items in a uniformly random order
 *
 * @author Amy Kang
 *
 * @version November 19, 2021
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item is null");
        queue[n++] = item;
        if (n == queue.length) resize(queue.length * 2);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(n);
        Item item = queue[random];
        queue[random] = queue[n - 1];
        queue[n - 1] = null;
        n--;
        if (n > 0 && n == queue.length / 4) resize(queue.length / 2);
        return item;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        if (n >= 0) System.arraycopy(queue, 0, copy, 0, n);
        queue = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return queue[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private final int[] indexes;
        private int current;

        public RandomIterator() {
            current = 0;
            indexes = new int[n];
            for (int i = 0; i < n; i++) {
                indexes[i] = i;
            }
            StdRandom.shuffle(indexes);
        }

        public boolean hasNext() {
            return current <= indexes.length - 1;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return queue[indexes[current++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private void print() {
        for (Item item : queue) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rqstr = new RandomizedQueue<>();
        rqstr.enqueue("1");
        rqstr.enqueue("2");
        rqstr.enqueue("3");
        rqstr.enqueue("4");
        rqstr.print();

        StdOut.println("removed: " + rqstr.dequeue());
        rqstr.print();
        StdOut.println("removed: " + rqstr.dequeue());
        rqstr.print();
        StdOut.println("removed: " + rqstr.dequeue());
        rqstr.print();
        System.out.println("sample: " + rqstr.sample());
        rqstr.print();

        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
