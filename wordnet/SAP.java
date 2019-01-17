import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.AbstractMap;
import java.util.HashMap;


// public class SAP {

//    // constructor takes a digraph (not necessarily a DAG)
//    public SAP(Digraph G)

//    // length of shortest ancestral path between v and w; -1 if no such path
//    public int length(int v, int w)

//    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
//    public int ancestor(int v, int w)

//    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
//    public int length(Iterable<Integer> v, Iterable<Integer> w)

//    // a common ancestor that participates in shortest ancestral path; -1 if no such path
//    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)

//    // do unit testing of this class
//    public static void main(String[] args)
// }

public class SAP {
    private Digraph graph;
    private int size;
    private Map<Integer, Integer> ancestorMap;
    
    private class AncestorPair implements Comparable<AncestorPair> {
        Map.Entry<Integer, Integer> pair;

        public AncestorPair(int ancestor, int dist) {
            this.pair = new AbstractMap.SimpleImmutableEntry<>(ancestor, dist);
        }

        public int compareTo(AncestorPair y) {
            return this.pair.getValue() - y.pair.getValue();
        }

        public int getDist() { return this.pair.getValue(); }
        public int getKey() { return this.pair.getKey(); }
    }

    public SAP(Digraph g) {
        this.graph = new Digraph(g);
        this.size = this.graph.V();
        this.ancestorMap = new HashMap<Integer, Integer>();
    }

    public int length(int v, int w) {
        int key = v + this.size*w;
        if (!this.ancestorMap.containsKey(key))
            ancestorHelper(v, w);
        return this.ancestorMap.get(key)/this.size;
    }

    public int ancestor(int v, int w) {
        int key = v + this.size*w;
        if (!this.ancestorMap.containsKey(key))
            ancestorHelper(v, w);
        return this.ancestorMap.get(key)%this.size;
    }

    private AncestorPair ancestorHelper(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(this.graph, w);
        ArrayList<AncestorPair> pairs = new ArrayList<AncestorPair>();

        // StdOut.println(this.size);
        for (int i = 0; i < this.size; i++) {
            if (bfsV.hasPathTo(i))
                if (bfsW.hasPathTo(i)) {
                    int d = bfsV.distTo(i) + bfsW.distTo(i);
                    pairs.add(new AncestorPair(i, d));
                }
        }

        Collections.sort(pairs);

        AncestorPair shortest = pairs.get(0);
        int key = v + this.graph.V()*w;
        int val = shortest.getKey() + this.graph.V()*shortest.getDist();
        ancestorMap.put(key, val);
        return shortest;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        ArrayList<AncestorPair> pairs = new ArrayList<AncestorPair>();

        for (Integer iv : v)
            for (Integer iw : w) {
                pairs.add(ancestorHelper(iv, iw));
            }
        Collections.sort(pairs);

        return pairs.get(0).getDist();
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        ArrayList<AncestorPair> pairs = new ArrayList<AncestorPair>();

        for (Integer iv : v)
            for (Integer iw : w) {
                pairs.add(ancestorHelper(iv, iw));
            }
        Collections.sort(pairs);

        return pairs.get(0).getKey();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        StdOut.println(G);
        int length = sap.length(13, 10);
        StdOut.printf("length = %d\n", length);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

