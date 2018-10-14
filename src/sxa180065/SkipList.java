// Change this to netid of any member of team
package sxa180065;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;
    Entry head, tail;  // dummy nodes head and tail
    int size;          //size of the skipList
    int maxLevel;      
    Entry[ ] last;   // used by find()
    Random random;

    static class Entry<E> {
	E element;
	Entry[] next;
	Entry prev;
	int span[];

	public Entry(E x, int lev) {
	    element = x;
	    next = new Entry[lev];
	    // add more code if needed
	}

	public E getElement() {
	    return element;
	}
    }

    // Constructor
    public SkipList() {
    	head = new Entry(null, PossibleLevels);
    	tail = new Entry(null, PossibleLevels);
    	size = 0;
    	maxLevel = 1;
    	last = new Entry[PossibleLevels];
    	random = new Random();
    	//setting head.next to tail
    	for(int i = 0; i < PossibleLevels; i++)
    	{
    		head.next[i] = tail;
    	}
    	tail.prev = head;
    }
    
    //Helper function to search for element x
	public void find(T x)
    {
    	Entry temp = head;
    	int i = maxLevel - 1;
    	while(i >= 0)
    	{
    		while(temp.next[i] != null && x.compareTo((T) (temp.next[i].element)) > 0)
    		{
    			temp = temp.next[i];
    		}
    		last[i] = temp;
    		i--;
    	}
    }
	
	//Choose the Level of an entry
	public int chooseLevel() {
		int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
		if(level > maxLevel) 
			maxLevel = level;
		return level;
	}

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {
    if(contains(x))
    	return false;
    int level = chooseLevel();
    Entry ent = new Entry(x, level);
    for(int i = 0;i < level; i++)
    {
    	ent.next[i] = last[i].next[i];
    	last[i].next[i] = ent;
    }
    ent.next[0].prev = ent;
    ent.prev = last[0];
    size += 1;
	return true;
    }

    // Find smallest element that is greater or equal to x
public T ceiling(T x) {
	return (T) last[0].next[0].element;
 }
    // Does list contain x?
    public boolean contains(T x) {
    find(x);  
	return last[0].next[0] == x;
    }

    // Return first element of list
    public T first() {
	//if(size==0) { return null; }
	return head.next[0].element;;
    }

    // Find largest element that is less than or equal to x
     public T floor(T x) {
	 if(contains(x)==true){
		 return (T) x;
	 }
	return (T) last[0].element;
	
 }
    // Return element at index n of list.  First element is at index 0.
    public T get(int n) {
	return null;
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
	if(n < 0 || n > size-1) 
		throw new NoSuchElementException();
	Entry temp = head;
	for(int i = 0;i < n;i++)
	{
		temp = temp.next[0];
	}
	return (T) temp.element;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.
    public T getLog(int n) {
        return null;
    }

    // Is the list empty?
    public boolean isEmpty() {
	return size == 0;
    }

    // Iterate through the elements of list in sorted order
    public Iterator<T> iterator() {
	return null;
    }

    // Return last element of list
    public T last() {
    return (T) tail.prev.element;
    }

    // Optional operation: Reorganize the elements of the list into a perfect skip list
    // Not a standard operation in skip lists. Eligible for EC.
    public void rebuild() {
	
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
    if(!contains(x))
    	return null;
    Entry ent = last[0].next[0];
    int len = ent.next.length;
    for(int i = 0;i < len; i++)
    {
    	last[i].next[i] = ent.next[i];
    }
    size -= 1;
	return (T) ent.element;
    }

    // Return the number of elements in the list
    public int size() {
	return size;
    }
}
