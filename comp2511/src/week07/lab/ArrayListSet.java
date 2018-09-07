package week07.lab;
import java.util.*;

/**
 * An implementation of a set using an ArrayList
 * @author bradfordh
 *
 * @param <E> uses a generic type so any object can be declared of this type
 */
public class ArrayListSet<E> implements Set<E> {
	
	
	/**
	 * Default constructor builds an empty set
	 */
	private ArrayList<E> item;
	public ArrayListSet() {
		// To be completed
		this.item = new ArrayList<E>();
	}

	/**
	 * Copy constructor, makes a copy of the inputed set.
	 * @param arrayListSet
	 */
	public ArrayListSet(ArrayListSet<E> arrayListSet) {
		// To be completed
		this.item = arrayListSet.item;
	}


	@Override
	public void add(E e) {
		// To be completed	
		if(!this.contains(e)) this.item.add(e);
	}

	@Override
	public void remove(E e) {
		// To be completed
		if (!(this.contains(e))) return;
		this.item.remove(e);
	}

	@Override
	public Set<E> union(Set<E> secondSet) {
		// To be completed
		ArrayListSet<E> r = new ArrayListSet<E>(this);
		Iterator<E> it = secondSet.iterator();
		while(it.hasNext()) {
			r.add(it.next());
		}
		return r;
	}

		
	@Override
	public Set<E> intersection(Set<E> secondSet) {
		// To be completed
		
		if (this.subset(secondSet)) return this;
		
		ArrayListSet<E> r = new ArrayListSet<E>();
		Iterator<E> it = this.iterator();
		while (it.hasNext()) {	
			E o = it.next();
			if (secondSet.contains(o)) r.add(o);
		}
		return r;
	}

	@Override
	public boolean contains(Object e) {
		// To be completed
		Iterator<E> it = this.iterator();
		while (it.hasNext()) {
			E target = it.next();
			if (e.equals(target)) return true;	
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return this.item.iterator();
	}
	
	@Override
	public String toString() {
		// To be completed
		StringBuilder sb = new StringBuilder();
		sb.append("Apples:");
		
		Iterator<E> it = this.iterator();
		while(it.hasNext()) {
			String s = (String)it.next().toString();
			sb.append(" ").append(s);
		}
		String result = new String(sb);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof Set<?>)) return false;
		ArrayListSet<?> als = (ArrayListSet<?>) o;
		Iterator<?> it = als.iterator();
		while (it.hasNext()) {
			if (!(this.contains(it.next()))) return false;
		}
		return true;
	}

	
	@Override
	public boolean subset(Set<E> secondSet)
	{
		for (E e : this)					// can do this since Set<E> implements Iterable<E>!!
			if (!secondSet.contains(e))
				return false;
		return true;
	}
		
	@Override
	public int size() {
		return this.item.size();
	}
}
