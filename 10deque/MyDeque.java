import java.util.NoSuchElementException;
public class MyDeque<E> {

	private E[] objects;
	private int head, size, capacity;

	@SuppressWarnings("unchecked")
	public MyDeque(int c) {
		objects = (E[]) new Object[c];
		capacity = c;
	}

	@SuppressWarnings("unchecked")
	public MyDeque() {
		objects = (E[]) new Object[10];
		capacity = 10;
	}

	@SuppressWarnings("unchecked")
	private void resize() {
		E[] doubleObjects = (E[]) new Object[2 * capacity];
		for (int i = 0; i < size; i++)
			doubleObjects[i] = objects[(head + i) % capacity];
		objects = doubleObjects;
		head = 0;
		capacity *= 2;
	}

	public void addFirst(E e) {
		if (size == capacity)
			resize();
		if (head == 0)
			head = capacity - 1;
		else
			head = (head - 1) % capacity;
		objects[head] = e;
		size++;
	}

	public void addLast(E e) {
		if (size == capacity)
			resize();
		objects[(head + size) % capacity] = e;
		size++;
	}

	public E removeFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		E value = objects[head];
		head = (head + 1) % capacity;
		size--;
		return value;
	}

	public E removeLast() {
		if (size == 0)
			throw new NoSuchElementException();
		E value = objects[(head + size - 1) % capacity];
		size--;
		return value;
	}

	public E getFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		return objects[head];
	}

	public E getLast() {
		if (size == 0)
			throw new NoSuchElementException();
		return objects[(head + size - 1) % capacity];
	}

	public int size() {
		return size;
	}

	public String toString() {
		String result = "[ ";
		for (int i = 0; i < size - 1; i++)
			result += objects[(head + i) % capacity] + ", ";
		return result + objects[(head + size - 1) % capacity] + " ]";
	}

}
