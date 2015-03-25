import java.lang.reflect.Array;
import java.lang.Class;
public class MyDeque<E> {
	private E[] objects;
    public MyDeque(Class<E> c, int capacity) {
        @SuppressWarnings("unchecked")
        E[] objects = (E[]) Array.newInstance(c, capacity);
        this.objects = objects;
    }
	public MyDeque() {
		// lol
	}
}
