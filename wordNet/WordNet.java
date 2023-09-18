import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Bag<Integer>> nouns;
    private final ArrayList<String> synsets;
    private final Digraph hypernyms;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        this.nouns = new ST<>();
        this.synsets = new ArrayList<>();
        constructSynsets(new In(synsets));
        this.hypernyms = new Digraph(this.nouns.size());
        constructDigraph(new In(hypernyms));
        sap = new SAP(this.hypernyms);
    }

    private void check(Digraph G) {
        if (G == null) throw new IllegalArgumentException("invalid digraph");
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) throw new IllegalArgumentException("digraph is not a DAG");
    }

    // constructs symbol table representing synsets
    private void constructSynsets(In in) {
        int id;
        String[] nounSet;
        String currentLine;

        while (in.hasNextLine()) {
            currentLine = in.readLine();
            id = Integer.parseInt(currentLine.split(",")[0]);
            nounSet = currentLine.split(",")[1].split(" ");
            for (String n : nounSet) {
                if (this.nouns.contains(n)) {
                    Bag<Integer> ids = this.nouns.get(n);
                    ids.add(id);
                }
                else {
                    Bag<Integer> ids = new Bag<>();
                    ids.add(id);
                    this.nouns.put(n, ids);
                }
            }

            this.synsets.add(currentLine.split(",")[1]);
        }
    }

    // constructs digraph
    private void constructDigraph(In in) {
        int id;
        String[] ids;
        String currentLine;
        int roots = 0;        // counts the number of roots in the digraph

        while (in.hasNextLine()) {
            currentLine = in.readLine();
            if (currentLine.contains(",")) {
                id = Integer.parseInt(currentLine.split(",")[0]);
                ids = currentLine.split(",");
                for (int i = 1; i < ids.length; i++) {
                    this.hypernyms.addEdge(id, Integer.parseInt(ids[i]));
                }
            }
            else {
                roots++;
            }
        }

        if (roots != 1) throw new IllegalArgumentException("not a rooted DAG");
        check(this.hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !(isNoun(nounB))) throw new IllegalArgumentException();
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !(isNoun(nounB))) throw new IllegalArgumentException();
        int ancestor = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return synsets.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
