import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        String[] input = StdIn.readAllStrings();
        RandomizedQueue<String> r = new RandomizedQueue<String>();
        for (String s: input)
        {
            r.enqueue(s);
        }
        for (int i = 0; i < k; i++)
        {
            StdOut.println(r.dequeue());
        }
    }
}