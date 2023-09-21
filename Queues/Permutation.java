import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Algorithms Week 2 Project -- Randomized Deques and Queues
 *
 * Using a randomized queue, this program outputs a uniformly random permutation of the inputted strings from a
 * file input.
 *
 * @author Amy Kang
 *
 * @version November 19, 2021
 */
public class Permutation {

    /**
     * prints a random permutation of strings
     *
     * @param args the number of random outputs followed by the file input containing the sequence of strings
     */
    public static void main(String[] args) {
        int numInputs = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) { // enqueues inputs
            randomizedQueue.enqueue(StdIn.readString());
        }

        int n = 0;
        for (String s : randomizedQueue) {
            if (n < numInputs) {
                StdOut.println(s);
                n++;
            }
        }

    }

}
