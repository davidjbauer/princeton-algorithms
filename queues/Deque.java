import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
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
   // I'm going to implement the deque using a doubly-linked list,
   // so first I'll need to implement the linked list Node.
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
   // store number of items, and head and tail node objects.
   private int numItems;
   private Node head;
   private Node tail;

   public Deque()
   {
        numItems = 0;
        head = null;
   }

   public boolean isEmpty()
   {
        if (numItems== 0)
            return true;
        else return false;
   }

   public int size()
   {
        return numItems;
   }

   public void addFirst(Item item)
   {
        if (head == null) {
            head = new Node(item);
            tail = head;
        }
        else {
            Node newNode = new Node(item);
            head.last = newNode;
            newNode.next = head;
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
        if(head == null)
            tail = null;
        numItems--;
        return tmpItem;
   }

   public Item removeLast()
   {
        if (head == null)
            throw new java.util.NoSuchElementException();
        Item tmpItem = tail.value;
        tail = tail.last;
        if (tail != null)
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
        { return false; }
        public void remove() { }
        public Item next() {
            Item tmpItem = current.value;
            current = current.next;
            return tmpItem;
        }

   }

   public static void main(String[] args)
   {
        Deque<Integer> d = new Deque<Integer>();
        StdOut.println("Created new empty deque. Check if empty:");
        StdOut.printf("Empty (should be true)? %b Size (should be 0)? %d\n", d.isEmpty(), d.size());
        StdOut.println("Adding items to front of queue in order: 1, 3, 5.");
        d.addFirst(1);
        d.addFirst(3);
        d.addFirst(5);
        StdOut.printf("Empty (should be false)? %b Size (should be 3)? %d\n", d.isEmpty(), d.size());
        StdOut.println("Adding items to back of queue in order: 2, 4, 6");
        d.addLast(2);
        d.addLast(4);
        d.addLast(6);
        StdOut.printf("Empty (should be false)? %b Size (should be 6)? %d\n", d.isEmpty(), d.size());
        StdOut.println("Remove items from front of queue. Should see: 5, 3, 1, 2.");
        StdOut.printf("Removing: %d, %d, %d, %d.\n", d.removeFirst(), d.removeFirst(), d.removeFirst(), d.removeFirst());
        StdOut.printf("Empty (should be false)? %b Size (should be 2)? %d\n", d.isEmpty(), d.size());
        StdOut.println("Adding items to front of queue in order: 10, 20.");
        d.addFirst(10);
        d.addFirst(20);
        StdOut.println("Remove items from back of queue. Should see: 6, 4, 10, 20.");
        StdOut.printf("Removing: %d, %d, %d, %d.\n", d.removeLast(), d.removeLast(), d.removeLast(), d.removeLast());
        StdOut.printf("Empty (should be true)? %b Size (should be 0)? %d\n", d.isEmpty(), d.size());

   }

}