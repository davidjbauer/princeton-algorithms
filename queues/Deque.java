import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

/*
   API Specification:
   public Deque()                           // construct an empty deque
   public boolean isEmpty()                 // is the deque empty?
   public int size()                        // return the number of items on the deque
   public void addFirst(Item item)          // add the item to the front
   public void addLast(Item item)           // add the item to the end
   public Item removeFirst()                // remove and return the item from the front
   public Item removeLast()                 // remove and return the item from the end
   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   public static void main(String[] args)   // unit testing (required)
*/
public class Deque<Item> implements Iterable<Item> {

  private int numItems;
  private Node head;
  private Node tail;

  // Implement Node class for doubly-linked list.
  private class Node
  {
    public Node next;
    public Node last;
    public Item value;

    public Node()
    {
      next = null;
      last = null;
    }
    public Node(Item val)
    {
      value = val;
      next = null;
      last = null;
    }
  }

  public Deque()
  {
    numItems = 0;
    head = null;
  }

  public boolean isEmpty()
  {
    if (numItems == 0) return true;
    else return false;
  }

  public int size()
  {
    return numItems;
  }

  public void addFirst(Item item)
  {
    if (item == null)
      throw new java.lang.IllegalArgumentException();
    if (head == null) {
      head = new Node(item);
      tail = head;
    }
    else {
      Node newNode = new Node(item);
      head.last = newNode;
      newNode.next = head;
      newNode.last = null;
      head = newNode;
    }
    numItems++;
  }

  public void addLast(Item item)
  {
    if (item == null)
      throw new java.lang.IllegalArgumentException();
    if (head == null) {
      head = new Node(item);
      tail = head;
    }
    else {
      Node newNode = new Node(item);
      tail.next = newNode;
      newNode.last = tail;
      newNode.next = null;
      tail = newNode;
    }
    numItems++;
  }

  public Item removeFirst()
  {
    if (head == null)
      throw new java.util.NoSuchElementException();
    Item tmpItem = head.value;
    head = head.next;
    if (head == null)
      tail = null;
    else
      head.last = null;
    numItems--;
    return tmpItem;
  }

  public Item removeLast()
  {
    if (head == null)
      throw new java.util.NoSuchElementException();
    Item tmpItem = tail.value;
    tail = tail.last;
    if (tail == null)
      head = null;
    else
      tail.next = null;
    numItems--;
    return tmpItem;
  }

  public Iterator<Item> iterator()
  {
    return new DequeIterator();
  }

  private class DequeIterator implements Iterator<Item>
  {
    private Node current = head;

    public boolean hasNext()
    { return (current != null); }
    public void remove()
    { throw new java.lang.UnsupportedOperationException(); }
    public Item next() {
      if (current == null)
        throw new java.util.NoSuchElementException();
      Item tmpItem = current.value;
      current = current.next;
      return tmpItem;
    }
  }

  public static void main(String[] args)
  {
    Deque<Integer> d = new Deque<Integer>();
    d.addFirst(0);
    StdOut.println(d.removeLast());
    d.addFirst(2);
    StdOut.println(d.removeLast());
    d.addFirst(1);
    d.addFirst(3);
    d.addFirst(5);
    d.addFirst(7);
    d.addFirst(9);
    d.addFirst(11);
    Iterator<Integer> it = d.iterator();
    while (it.hasNext()) {
      StdOut.println(it.next());
    }

  }

}