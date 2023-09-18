import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        int currentDistance;
        String currentOutcast = nouns[0];

        for (int i = 0; i < nouns.length; i++) {
            currentDistance = distance(nouns[i], nouns);
            if (currentDistance > maxDistance) {
                maxDistance = currentDistance;
                currentOutcast = nouns[i];
            }
        }

        return currentOutcast;
    }

    // calculate the sum of distances between this noun and other nouns
    private int distance(String noun, String[] nouns) {
        int totalDistance = 0;
        for (String otherNoun : nouns) {
            totalDistance += wordNet.distance(noun, otherNoun);
        }
        return totalDistance;
    }


    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
