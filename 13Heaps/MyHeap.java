public class MyHeap {
	private int[] nums;
	private boolean max;

	public MyHeap(boolean isMax) {
		max = isMax;
		nums = new int[8];
	}

	public MyHeap() {
		new MyHeap(true);
	}

	public int peek() {
		if (nums[0] < 1)
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
		if (max)
			while (pos > 1 && nums[pos] > nums[pos / 2]) {
				temp = nums[pos];
				nums[pos] = nums[pos / 2];
				nums[pos / 2] = temp;
				pos /= 2;
			}
		else
			while (pos > 1 && nums[pos] < nums[pos / 2]) {
				temp = nums[pos];
				nums[pos] = nums[pos / 2];
				nums[pos / 2] = temp;
				pos /= 2;
			}
	}

	public int remove() {
		int pos = nums[0]--;
		int replacement = nums[pos];
		int previous;
		while (pos > 1) {
			previous = nums[pos / 2];
			nums[pos / 2] = replacement;
			replacement = previous;
			pos /= 2;
		}
		return replacement;
	}

	private int maxLength() {
		// returns the number of digits in the biggest integer in MyHeap
		int result = nums[1];
		for (int i = 2; i <= nums[0]; i++)
			if (nums[i] > result)
				result = nums[i];
		return (int) Math.log10(result) + 1;
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
		int maxLength = maxLength();
		String result = "";
		int pos = 1;
		for (int level = 1; Math.pow(2, level) <= nums[0]; level++) {
			result += spaces(maxLength * Math.pow(2, level - 1) - maxLength);
			while (pos < Math.pow(2, level) - 1)
				result +=
					nums[pos] +
					spaces(maxLength - 1 - (int) Math.log10(nums[pos++])) +
					spaces(maxLength * Math.pow(2, level) - maxLength);
			result += nums[pos++] + "\n";
		}
		return result;
	}

}
