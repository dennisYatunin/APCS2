public class MyLinkedList<E> {

	private class LNode<E> {
		private E data;
		private LNode<E> next;
		public LNode(E d) {
			data = d;
		}
		public String toString() {
			return data.toString();
		}
		public E getData() {
			return data;
		}
		public LNode<E> getNext() {
			return next;
		}
		public void setData(E d) {
			data = d;
		}
		public void setNext(LNode<E> n) {
			next = n;
		}
	}

	private LNode<E> head;
	private LNode<E> tail;
	private int size;

	public String name() {
		return "yatunin.dennis";
	}

	public int size() {
		return size;
	}

	public String toString() {
		String result = "[ ";
		LNode<E> pointer = head;
		while (!pointer.equals(tail)) {
			result += pointer + ", ";
			pointer = pointer.getNext();
		}
		return result + tail + " ]";
	}

	public E get(int p) {
		if (p >= size)
			throw new java.lang.IndexOutOfBoundsException();
		if (p == 0)
			return head.getData();
		if (p == size - 1)
			return tail.getData();
		LNode<E> pointer = head;
		for (int i = 1; i < p; i++)
			pointer = pointer.getNext();
		return pointer.getNext().getData();
	}

	public E set(int p, E d) {
		if (p >= size)
			throw new java.lang.IndexOutOfBoundsException();
		if (p == 0) {
			E result = head.getData();
			head.setData(d);
			return result;
		}
		if (p == size - 1) {
			E result = tail.getData();
			tail.setData(d);
			return result;
		}
		LNode<E> pointer = head;
		for (int i = 1; i <= p; i++)
			pointer = pointer.getNext();
		E result = pointer.getData();
		pointer.setData(d);
		return result;
	}

	public boolean add(E d) {
		if (head == null) {
			head = new LNode<E>(d);
			tail = head;
		}
		else {
			tail.setNext(new LNode<E>(d));
			tail = tail.getNext();
		}
		size++;
		return true;
	}

	public void add(int p, E d) {
		if (p > size)
			throw new java.lang.IndexOutOfBoundsException();
		if (p == size) {
			add(d);
			return;
		}
		LNode<E> newGuy = new LNode<E>(d);
		if (p == 0) {
			newGuy.setNext(head);
			head = newGuy;
		}
		else {
			LNode<E> pointer = head;
			for (int i = 1; i < p; i++)
				pointer = pointer.getNext();
			newGuy.setNext(pointer.getNext());
			pointer.setNext(newGuy);
		}
		size++;
	}

	public E remove(int p) {
		if (p >= size)
			throw new java.lang.IndexOutOfBoundsException();
		if (p == 0) {
			E result = head.getData();
			head = head.getNext();
			size--;
			return result;
		}
		LNode<E> pointer = head;
		for (int i = 1; i < p; i++)
			pointer = pointer.getNext();
		E result = pointer.getNext().getData();
		pointer.setNext(pointer.getNext().getNext());
		if (p == --size)
			tail = pointer;
		return result;
	}

	public int indexOf(E d) {
		LNode<E> pointer = head;
		for (int i = 0; i < size; i++) {
			if (pointer.getData().equals(d))
				return i;
			pointer = pointer.getNext();
		}
		return -1;
	}

}
