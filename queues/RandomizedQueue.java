import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
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
  private static final int initialCapacity = 5;

  public RandomizedQueue()
  {
    capacity = initialCapacity;
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
    int tmpIndex = 0;
    for (int i = 0; i < numItems; i++) {
        tmpArray[i] = array[i];
    }
    array = tmpArray;
    capacity = newSize;
  }

  public void enqueue(Item item)
  {
    if (item == null)
      throw new java.lang.IllegalArgumentException();
    if (numItems == capacity) resize(2*capacity);
    array[numItems++] = item;
  }

  public Item dequeue()
  {
    if(numItems == 0)
      throw new java.util.NoSuchElementException();

    int randInt = StdRandom.uniform(numItems);
    Item returnItem = null;

    returnItem = array[randInt];
    array[randInt] = array[numItems - 1];
    array[numItems - 1] = null;

    numItems--;
    if (1.0*numItems / capacity <= 1/4)
      resize(capacity/2);
    return returnItem;
  }

  public Item sample()
  {
    if (numItems == 0)
      throw new java.util.NoSuchElementException();
    int randInt = StdRandom.uniform(numItems);
    if (array[randInt] != null)
      return array[randInt];
    else return sample();
  }

  private class RandomizedQueueIterator implements Iterator<Item>
  {
    public boolean hasNext()
    { return numItems != 0; }
    public void remove() 
    { throw new java.lang.UnsupportedOperationException(); }
    public Item next() 
    { 
      if (numItems == 0 )
        throw new java.util.NoSuchElementException();
      return dequeue();
    }
  }

  public Iterator<Item> iterator()
  { return new RandomizedQueueIterator(); }

  public static void main(String[] args)
  {
    RandomizedQueue<Integer> r = new RandomizedQueue<Integer>();
    for(int i = 0; i < 10000; i++)
      r.enqueue(i);
    for(int j = 0; j < 9999; j++)
      StdOut.printf("%d, ", r.dequeue());
  }
}