import java.util.LinkedList;
public class MyQueuek<E> {
	LinkedList<E> objects = new LinkedList<E>();
	public boolean add(E e) {
		objects.add(0, E);
		return true;
	}
	public E element() {
		return objects.get(0);
	}
	public boolean offer(E e) {
		objects.add(0, E);
		return true;
	}
	public E peek() {
		return objects.get(0);
	}
	public E poll() {
		objects.remove(0);
		return item;
	}
	public E remove() {
		objects.remove(0);
		return item;
	}
}
