package alde.commons.util.as3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * A wrapper for ConcurrentHashMap that simulates AS3's Vector
 * Simulates a List that can be used as <Index, Value>
 */
public class Vector<T> implements Iterable<T> {

	public int length;
	private ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();

	public Vector() {
		this(0);
	}

	public Vector(int initialCapacity) {
		map = new ConcurrentHashMap<>(initialCapacity);
	}

	@SafeVarargs
	public Vector(T... addAll) {
		add(addAll);
	}

	public Vector(List<T> addAll) {
		for (T t : addAll) {
			push(t);
		}
	}

	public Vector(int numMembers, boolean fixedLength) {
		this(numMembers);
	}

	private void updateLength() {
		this.length = map.size();
	}

	/**
	 * Removes object at index
	 */
	public T remove(int i) {
		return map.remove(i);
	}

	/**
	 * Removes object
	 */
	public void remove(T t) {
		Iterator<T> e = this.iterator();

		for (Iterator<T> it = e; it.hasNext();) {
			T a = it.next();
			if (a.equals(t)) {
				it.remove();
			}
		}
	}

	public boolean contains(T t) {
		return map.containsValue(t);
	}

	public void push(T t) {
		int newSize = map.size() == 0 ? 0 : map.size() + 1;
		put(newSize, t);
	}

	public void set(int index, T t) {
		put(index, t);
	}

	public T put(int index, T t) {
		map.put(index, t);
		updateLength();

		return t;
	}

	public T get(int index) {
		return map.get(index);
	}

	//Removes the last element from the Vector and returns that element.
	public T pop() {
		if (length > 0) {
			length--;
		}
		updateLength();
		return map.remove(map.size());
	}

	@Override
	public Iterator<T> iterator() {
		return map.values().iterator();
	}

	public void add(T t) {
		push(t);
	}

	public void add(T... list) {
		for (T t : list) {
			push(t);
		}
	}

	/*
	 * Concatenates the Vectors specified in the parameters list with the elements in this Vector and creates a new Vector.
	 */
	public final Vector<T> concat(Vector<T>... vectors) {
		List<T> data = new ArrayList<>();

		for (Vector<T> vec : vectors) {
			for (T t : vec) {
				data.add(t);
			}
		}

		updateLength();

		return new Vector<T>(data);
	}

	public void clear() {
		map.clear();
	}

	public boolean hasOwnProperty(T i) {
		return contains(i);
	}

	public int indexOf(T loc2) {
		for (int i : map.keySet()) {
			if (map.get(i).equals(loc2)) {
				return i;
			}
		}
		return -1;
	}

	public void set(T set) {
		clear();
		add(set);
	}

	public int size() {
		return length;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("'");
		for (T t : map.values()) {
			s.append(t);
			s.append(", ");
		}
		s.append("'");
		return s.toString();
	}

	public Vector<T> slice() {
		return this;

	}

	@Deprecated
	public void sortOn(Vector<T> sortOnFields, Vector<T> sortOnParams) {
	}

	@Deprecated
	public void splice(T i, T i1, T lineTo, T lineTo1, T lineTo2) {
	}

	@Deprecated
	public void splice(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
	}

	@Deprecated
	public void splice(double start, double deleteCount) {
	}

	@Deprecated
	public void sort(BiFunction<T, T, Integer> sort) {

	}

	public boolean isEmpty() {
		return size() > 0;
	}

}
