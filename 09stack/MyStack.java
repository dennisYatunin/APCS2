import java.util.LinkedList;
public class MyStack<E> {
	LinkedList<E> objects = new LinkedList<E>();
	public boolean empty() {
		return objects.size() == 0;
	}
	public E peek() {
		return objects.get(0);
	}
	public E pop() {
		return objects.remove(0);
	}
	public E push(E item) {
		objects.add(0, item);
		return item;
	}
	public int search(Object o) {
		int answer = objects.indexOf(o);
		return (answer == -1) ? answer : answer++;
	}
}
