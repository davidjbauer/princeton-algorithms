import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Iterator;

/*
   public RandomizedQueue()                 // construct an empty randomized queue
   public boolean isEmpty()                 // is the randomized queue empty?
   public int size()                        // return the number of items on the randomized queue
   public void enqueue(Item item)           // add the item
   public Item dequeue()                    // remove and return a random item
   public Item sample()                     // return a random item (but do not remove it)
   public Iterator<Item> iterator()         // return an independent iterator over items in random order
   public static void main(String[] args)   // unit testing (required)
*/

public class RandomizedQueue<Item> implements Iterable<Item> {

   private Item[] array;
   private int numItems;
   private int capacity;

   public RandomizedQueue()
   {
      capacity = 5;
      array = (Item[]) new Object[capacity];
      numItems = 0;
   }

   public boolean isEmpty()
   { 
      if (numItems == 0) return true;
      else return false;
   }

   public int size()
   { return numItems; }

   private void resize(int newSize)
   {
      Item[] tmpArray = (Item[]) new Object[newSize];
      for (int i = 0; i < numItems; i++)
          tmpArray[i] = array[i];
      array = tmpArray;
      capacity = newSize;
   }

   public void enqueue(Item item)
   {
      if (numItems == capacity) resize(2*capacity);
      array[numItems++] = item;
   }

   public Item dequeue()
   {
      int randInt = StdRandom.uniform(numItems);
      Item returnItem = array[randInt];
      for (int i = randInt; i < (capacity - 1); i++)
      {
          if(array[i+1] != null)
              array[i] = array[i+1];
      }
      array[capacity - 1] = null;
      numItems--;
      if(1.0*numItems / capacity <= 1/4) resize(capacity/2);
      return returnItem;
   }

   private class RandomizedQueueIterator implements Iterator<Item>
   {
      public boolean hasNext()
      { return numItems != 0; }
      public void remove() { }
      public Item next() 
      { return dequeue(); }
   }

   public Iterator<Item> iterator()
   { return new RandomizedQueueIterator(); }

   public static void main(String[] args)
   {
        RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
        r.enqueue(1);
        r.enqueue(2);
        r.enqueue(3);
        r.enqueue(5);
        r.enqueue(7);
        r.enqueue(11);
        r.enqueue(13);
        StdOut.printf("Size (should be 7): %d\n", r.size());
        StdOut.printf("Dequeue 3 items:\n");
        StdOut.printf("%d, %d, %d\n", r.dequeue(), r.dequeue(), r.dequeue());
        StdOut.printf("Size (should be 4): %d\n", r.size());
   }

}