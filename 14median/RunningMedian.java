public class RunningMedian {

	MyHeap smallHalf;
	MyHeap largeHalf;
	int[] middle;

	public RunningMedian() {
		smallHalf = new MyHeap(true);
		largeHalf = new MyHeap(false);
		middle = new int[3];
	}

	public double getMedian() {
		switch (middle[0]) {
			case 1:
				return middle[1];
			case 2:
				return (middle[1] + middle[2]) / 2.;
			default:
				throw new java.util.NoSuchElementException();
		}
	}

	public void add(int i) {
		switch (middle[0]) {
			case 1:
				if (i < middle[1]) {
					if (smallHalf.size() == 0 || i > smallHalf.peek())
						middle[2] = i;
					else {
						middle[2] = smallHalf.remove();
						smallHalf.add(i);
					}
				}
				else if (i > middle[1]) {
					if (largeHalf.size() == 0 || i < largeHalf.peek())
						middle[2] = i;
					else {
						middle[2] = largeHalf.remove();
						largeHalf.add(i);
					}
				}
				else
					middle[2] = i;
				middle[0] = 2;
				break;
			case 2:
				if (i < middle[1] && i < middle[2]) {
					smallHalf.add(i);
					largeHalf.add(Math.max(middle[1], middle[2]));
					middle[1] = Math.min(middle[1], middle[2]);
				}
				else if (i > middle[1] && i > middle[2]) {
					largeHalf.add(i);
					smallHalf.add(Math.min(middle[1], middle[2]));
					middle[1] = Math.max(middle[1], middle[2]);
				}
				else {
					smallHalf.add(Math.min(middle[1], middle[2]));
					largeHalf.add(Math.max(middle[1], middle[2]));
					middle[1] = i;
				}
				middle[0] = 1;
				break;
			default:
				middle[1] = i;
				middle[0] = 1;
		}
	}

	public String toString() {
		String result = "Small Half:\n" + smallHalf + "\nMiddle:\n";
		switch (middle[0]) {
			case 1:
				result += middle[1] + "\n";
				break;
			case 2:
				result += middle[1] + "\n" + middle[2] + "\n";
				break;
			default:
				result += "null\n";
		}
		result += "\nLarge Half:\n" + largeHalf;
		return result;
	}

	public static void main(String[] args) {
		RunningMedian r = new RunningMedian();
		System.out.println(r);
		r.add(9);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(1);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(-2);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(90);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(3);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(-100);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
		r.add(0);
		System.out.println(r);
		System.out.println("Median: " + r.getMedian() + "\n");
	}

}
