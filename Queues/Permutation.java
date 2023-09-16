import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
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
