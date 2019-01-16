import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
/* 
public class WordNet {

   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms)

   // returns all WordNet nouns
   public Iterable<String> nouns()

   // is the word a WordNet noun?
   public boolean isNoun(String word)

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB)

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   public String sap(String nounA, String nounB)

   // do unit testing of this class
   public static void main(String[] args)
}
*/

public class WordNet {

   private Digraph hypernymGraph;
   private Map<String, Integer> nounMap;
   private SAP wordNetSAP;
   private ArrayList<String> synsetList;

   public WordNet(String synsets, String hypernyms) {
      this.nounMap = new HashMap<String, Integer>();

      In synsetFile = new In(synsets);
      In hypernymFile = new In(hypernyms);
      this.synsetList = new ArrayList<String>();
      String currentLine;
      String[] tokens;
      int numSynsets;

      // read synset file and store in hash table
      // keys are nouns, values are integers
      // allows us to store integers in our digraph
      // and use hash table to find these integers from
      // the synsets (synsetList lets us go the other way)
      currentLine = synsetFile.readLine();
      while (currentLine != null) {
         tokens = currentLine.split(",");
         String[] nouns = tokens[1].split(" ");
         this.synsetList.add(tokens[1]);
         for (String noun : nouns) {
            nounMap.put(noun, Integer.parseInt(tokens[0]));
         }
         currentLine = synsetFile.readLine();
      }

      numSynsets = this.synsetList.size();
      this.hypernymGraph = new Digraph(numSynsets);
      

      // build the hypernym graph
      currentLine = hypernymFile.readLine();
      while (currentLine != null) {
         tokens = currentLine.split(",");
         int source = Integer.parseInt(tokens[0]);
         for (String token : tokens) {
            int key = Integer.parseInt(token);
            if (key != source) {
               hypernymGraph.addEdge(source, key);
            }
         }
         currentLine = hypernymFile.readLine();
      }
      this.wordNetSAP = new SAP(this.hypernymGraph);

   }

   public Iterable<String> nouns() {
      return (Iterable<String>)this.nounMap.keySet();
   }
   
   public boolean isNoun(String word) {
      if (this.nounMap.get(word) == null)
         return false;
      else return true;
   }

   public int distance(String nounA, String nounB) {
      int keyA = nounMap.get(nounA);
      int keyB = nounMap.get(nounB);
      return this.wordNetSAP.length(keyA, keyB);
   }

   public String sap(String nounA, String nounB) {
      int keyA = nounMap.get(nounA);
      int keyB = nounMap.get(nounB);
      int keySAP =  this.wordNetSAP.ancestor(keyA, keyB);
      String nounSAP = this.synsetList.get(keySAP);
      return nounSAP;
   }

   public static void main(String[] args) {
      WordNet net = new WordNet(args[0], args[1]);
      while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length  = net.distance(v, w);
            String ancestor = net.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }

   }

}
