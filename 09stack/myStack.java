import java.util.LinkedList;
public class myStack<E> {
	LinkedList<E> objects = new LinkedList<E>();
	public boolean empty() {
		return objects.isEmpty();
	}
	public E peek() {
		return objects.getFirst();
	}
	public E pop() {
		return objects.removeFirst();
	}
	public E push(E item) {
		objects.addFirst(item);
		return item;
	}
	public int search(Object o) {
		int answer = objects.indexOf(o);
		return (answer == -1) ? answer : answer++;
	}
}
