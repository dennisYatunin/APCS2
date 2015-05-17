public class MyHeap {

	private int[] nums;
	private boolean max;

	public MyHeap(boolean isMax) {
		max = isMax;
		nums = new int[8];
	}

	public MyHeap() {
		this(true);
	}

	public int size() {
		return nums[0];
	}

	public int peek() {
		if (nums[0] == 0)
			throw new java.util.NoSuchElementException();
		return nums[1];
	}

	private void extend() {
		int[] newNums = new int[nums.length * 2];
		System.arraycopy(nums, 0, newNums, 0, nums.length);
		nums = newNums;
	}

	public void add(int i) {
		int pos = ++nums[0];
		if (pos == nums.length)
			extend();
		nums[pos] = i;
		int temp;
		while (
			pos > 1 &&
			((max) ?
				nums[pos] > nums[pos / 2] :
				nums[pos] < nums[pos / 2])
			) {
			// swap the int at pos with its parent int
			temp = nums[pos];
			nums[pos] = nums[pos / 2];
			nums[pos / 2] = temp;
			pos /= 2;
		}
	}

	public int remove() {
		if (nums[0] == 0)
			throw new java.util.NoSuchElementException();
		int result = nums[1];
		nums[1] = nums[nums[0]--];
		int pos = 1;
		int temp, newPos;
		while (2 * pos <= nums[0]) {
			if (2 * pos == nums[0])
				// if the int at pos only has one child,
				// make newPos the position of that child
				newPos = 2 * pos;
			else {
				// make newPos the position of pos's biggest or smallest
				// child, depending on whether it is a max or min heap
				if (nums[2 * pos] > nums[2 * pos + 1])
					newPos = (max) ? 2 * pos : 2 * pos + 1;
				else
					newPos = (max) ? 2 * pos + 1 : 2 * pos;
			}
			if ((max) ?
				nums[newPos] > nums[pos] :
				nums[newPos] < nums[pos]
				) {
				// swap the int at pos with the int at newPos
				temp = nums[newPos];
				nums[newPos] = nums[pos];
				nums[pos] = temp;
				// more swapping may still be needed, so keep on looping
				pos = newPos;
			}
			else
				// if no more swapping is needed, end the loop
				break;
		}
		return result;
	}

	private int maxLength() {
		// returns the greatest length of an int found in MyHeap
		int result = String.valueOf(nums[1]).length();
		for (int i = 2; i <= nums[0]; i++)
			if (String.valueOf(nums[i]).length() > result)
				result = String.valueOf(nums[i]).length();
		return result;
	}

	private String spaces(double n) {
		// returns a String of n spaces
		if (n == 0)
			return "";
		return String.format("%" + (int)n + "s", "");
	}

	public String toString() {
		if (nums[0] == 0)
			return "null\n";
		String result = "";
		int maxLevel;
		// find the number of levels in the heap
		for (
			maxLevel = 1;
			Math.pow(2, maxLevel + 1) <= 2 * nums[0];
			maxLevel++
			) {};
		int maxLength = maxLength();
		int pos = 1;
		for (int level = 1; level <= maxLevel; level++) {
			// add spaces to the left of each line to keep things centered
			result +=
				spaces(
					maxLength * Math.pow(2, maxLevel - level) -
					maxLength
					);
			while (pos < Math.pow(2, level) - 1 && pos < nums[0])
				result +=
					nums[pos] +
					spaces(
						maxLength -
						String.valueOf(nums[pos++]).length()
						) +
					spaces(
						maxLength * Math.pow(2, maxLevel - level + 1) -
						maxLength
						);
			result += nums[pos++] + "\n";
		}
		return result;
	}

	public static void main(String[] args) {
		MyHeap maxHeap = new MyHeap();
		System.out.println(maxHeap);
		maxHeap.add(10);
		System.out.println(maxHeap);
		maxHeap.add(2);
		System.out.println(maxHeap);
		maxHeap.add(-6);
		System.out.println(maxHeap);
		maxHeap.add(100);
		System.out.println(maxHeap);
		maxHeap.add(1);
		System.out.println(maxHeap);
		maxHeap.add(2);
		System.out.println(maxHeap);
		maxHeap.add(9);
		System.out.println(maxHeap);
		maxHeap.add(101);
		System.out.println(maxHeap);
		maxHeap.add(-20);
		System.out.println(maxHeap);
		maxHeap.add(-10);
		System.out.println(maxHeap);
		System.out.println("removing " + maxHeap.remove() + ":");
		System.out.println(maxHeap);
		System.out.println("removing " + maxHeap.remove() + ":");
		System.out.println(maxHeap);
		System.out.println("removing " + maxHeap.remove() + ":");
		System.out.println(maxHeap);
	}

}
