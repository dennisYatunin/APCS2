import java.util.NoSuchElementException;

public class MyDeque<E> {

	private E[] objects;
	private int head, tail, size, capacity;

	@SuppressWarnings("unchecked")
	public MyDeque(int c) {
		if (c < 1)
			throw new IllegalArgumentException();
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
		if (size < 25)
			for (int i = 0; i < size; i++)
				doubleObjects[i] = objects[(head + i) % capacity];
		else {
			if (tail >= head)
				System.arraycopy(objects, head, doubleObjects, 0, size);
			else {
				System.arraycopy(objects, head, doubleObjects, 0, capacity - head);
				System.arraycopy(objects, 0, doubleObjects, capacity - head, size - capacity + head);
			}
		}
		objects = doubleObjects;
		head = 0;
		tail = size - 1;
		capacity *= 2;
	}

	public void addFirst(E e) {
		if (size == capacity)
			resize();
		if (size > 0) {
			if (head == 0)
				head = capacity - 1;
			else
				head--;
		}
		objects[head] = e;
		size++;
	}

	public void addLast(E e) {
		if (size == capacity)
			resize();
		if (size > 0) {
			if (tail == capacity - 1)
				tail = 0;
			else
				tail++;
		}
		objects[tail] = e;
		size++;
	}

	public boolean add(E item) {
		addLast(item);
		return true;
	}

	public E removeFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		E value = objects[head];
		if (head == capacity - 1)
			head = 0;
		else
			head++;
		size--;
		return value;
	}

	public E removeLast() {
		if (size == 0)
			throw new NoSuchElementException();
		E value = objects[tail];
		if (tail == 0)
			tail = capacity - 1;
		else
			tail--;
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
		return objects[tail];
	}

	public int size() {
		return size;
	}

	public String toString() {
		String result = "[ ";
		for (int i = 0; i < size - 1; i++)
			result += objects[(head + i) % capacity] + ", ";
		return result + objects[tail] + " ]";
	}

}
