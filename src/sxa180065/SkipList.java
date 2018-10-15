package sxa180065;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
	Entry<T> head, tail; // dummy nodes head and tail
	int size; // size of the skipList
	int maxLevel;
	Entry<T>[] last; // used by find()
	int[] spans; // used by find() to store spans while traversing levels
	Random random;

	static class Entry<E> {
		E element;
		Entry<E>[] next;
		Entry<E> prev;
		int span[];

		public Entry(E x, int lev) {
			element = x;
			next = new Entry[lev];
			span = new int[lev];
		}

		public E getElement() {
			return element;
		}
	}

	// Constructor
	public SkipList() {
		head = new Entry<T>(null, PossibleLevels);
		tail = new Entry<T>(null, PossibleLevels);
		size = 0;
		maxLevel = 1;
		last = new Entry[PossibleLevels];
		spans = new int[PossibleLevels];
		random = new Random();
		// setting head.next to tail
		for (int i = 0; i < PossibleLevels; i++) {
			head.next[i] = tail;
		}
		tail.prev = head;
	}

	// Helper function to search for element x
	public void find(T x) {
		Entry<T> temp = head;
		int i = maxLevel - 1;
		while (i >= 0) {
			while (temp.next[i] != null && temp.next[i].element != null && x.compareTo(temp.next[i].element) > 0) {
				temp = temp.next[i];
			}
			last[i] = temp;
			i--;
		}
	}

	// Choose the Level of an entry
	public int chooseLevel() {
		int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
		if (level > maxLevel)
			maxLevel = level;
		return level;
	}

	// Add x to list. If x already exists, reject it. Returns true if new node is
	// added to list
	public boolean add(T x) {
		int level = chooseLevel();
		// Exit if already exist
		if (contains(x))
			return false;
		Entry<T> ent = new Entry<T>(x, level);
		for (int i = 0; i < level; i++) {
			ent.next[i] = last[i].next[i];
			ent.span[i] = last[i].span[i] == 1 ? 1 : last[i].span[i] - spans[i] + 1;
			last[i].next[i] = ent;
			last[i].span[i] = last[i].span[i] == 1 ? 1 : spans[i];
		}
		ent.next[0].prev = ent;
		ent.prev = last[0];
		size += 1;
		return true;
	}

	// Find smallest element that is greater or equal to x
	public T ceiling(T x) {
		if (contains(x))
			return x;
		return last[0].next[0].element;
	}

	// Does list contain x?
	public boolean contains(T x) {
		find(x);
		if (last[0].next[0].element == null)
			return false;

		return x.compareTo(last[0].next[0].element) == 0;
	}

	// Return first element of list
	public T first() {
		return head.next[0].element;
	}

	// Find largest element that is less than or equal to x
	public T floor(T x) {
		if (contains(x))
			return x;
		return last[0].element;
	}

	// Return element at index n of list. First element is at index 0.
	public T get(int n) {
		if (n < 0 || n >= size)
			throw new NoSuchElementException();
		return getLinear(n);
	}

	// O(n) algorithm for get(n)
	public T getLinear(int n) {
		Entry<T> temp = head;
		for (int i = 0; i <= n; i++) {
			temp = temp.next[0];
		}
		return temp.element;
	}

	// Optional operation: Eligible for EC.
	// O(log n) expected time for get(n). Requires maintenance of spans, as
	// discussed in class.
	public T getLog(int n) {
		Entry<T> temp = head;
		int i = maxLevel - 1;
		int distance = 0;
		while (i >= 0) {
			// if (distance + temp.next[i].span[i] == n) return temp.next[i].element;
			while (temp.next[i] != null && distance + temp.next[i].span[i] < n) {
				System.out.println("span: " + temp.next[i].span[i]);
				// System.out.println("" + (temp.next[i] == null));
				distance += temp.next[i].span[i];
				temp = temp.next[i];
			}
			System.out.println("n: " + n + " distance: " + distance + " sz: " + size);
			i--;
		}
		return null;
	}

	// Is the list empty?
	public boolean isEmpty() {
		return size == 0;
	}

	// Iterate through the elements of list in sorted order
	public Iterator<T> iterator() {
		return new SkipListIterator();
	}

	protected class SkipListIterator implements Iterator<T> {

		Entry<T> cursor;
		boolean ready;

		SkipListIterator() {
			cursor = head;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next[0].element != null;
		}

		public T next() {
			if (cursor.next == null)
				throw new NoSuchElementException();
			cursor = cursor.next[0];
			ready = true;
			return cursor.element;
		}

		public void remove() {
			if (!ready)
				throw new IllegalStateException();
			find(cursor.element);
			Entry<T> ent = last[0].next[0];
			int len = ent.next.length;
			for (int i = 0; i < len; i++) {
				last[i].next[i] = ent.next[i];
			}
			ent.next[0].prev = last[0];
			size -= 1;
			cursor = ent.prev;
			System.out.println("Current cursor "+ cursor.element);
			ready = false; // next() should be called atleast once inorder to call remove()
		}

	}

	// Return last element of list
	public T last() {
		return tail.prev.element;
	}

	// Optional operation: Reorganize the elements of the list into a perfect skip
	// list
	// Not a standard operation in skip lists. Eligible for EC.
	public void rebuild() {

	}

	// Remove x from list. Removed element is returned. Return null if x not in list
	public T remove(T x) {
		if (!contains(x))
			return null;
		Entry<T> ent = last[0].next[0];
		int len = ent.next.length;
		for (int i = 0; i < len; i++) {
			last[i].next[i] = ent.next[i];
			last[i].next[i].span[i] += ent.next[i].span[i];
		}
		ent.next[0].prev = last[0];
		size -= 1;
		return ent.element;
	}
	
	public  void printList() {
		Entry<T> temp = head.next[0];
		while(temp.element != null)
		{
			System.out.println(" element "+temp.element+" Level Size "+temp.next.length);
			temp = temp.next[0];
		}
	}

	// Return the number of elements in the list
	public int size() {
		return size;
	}

	public static void main(String[] args) throws NoSuchElementException {

		SkipList<Integer> sl = new SkipList<>();
		for (int i = 1; i <= 10; i++) {
			sl.add(Integer.valueOf(i));
		}
		
		Iterator<Integer> it = sl.iterator();
		
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the operation to be done\n"
    		+ "1. Move next\n"
    		+ "2. Add element\n"
    		+ "3. Remove\n"
    		+ "Default. Exit");
		
		whileloop:
		while(in.hasNext()) {
	    int com = in.nextInt();
	    switch(com) {
	    case 1:  // Move to next element and print it
			if (it.hasNext()) {
			    System.out.println(it.next());
			} else {
				System.out.println("Cursor is pointing to the tail node therefore it has no next");
			    break whileloop;
			}
		break;
	    case 2:  // Add element to the SkipList;
	    	com = in.nextInt();
	        sl.add(com);
			break;  
	    case 3: //Remove
	    	it.remove();
	    	break;
	    default:  // Exit loop
		 break whileloop;
	    }
	    sl.printList();
	    System.out.println("Enter the operation to be done\n"
	    		+ "1. Move next\n"
	    		+ "2. Add element\n"
	    		+ "3. Remove\n"
	    		+ "Default. Exit");
    }
		

	}
}