public class QuickSelect {

	private static int[] ary;
	private static int target, pivot, nD;

	public static int select(int[] a, int p) {
		ary = a;
		target = p;
		return select(0, ary.length - 1);
	}

	public static int select(int si, int ei) {
		partition(si, ei);
		if (target >= pivot && target <= pivot + nD)
			return ary[pivot];
		if (target < pivot)
			return select(si, pivot - 1);
		return select(pivot + nD + 1, ei);
	}

	public static void partition(int si, int ei) {

		int ri = si + (int)(Math.random() * (ei - si + 1));
		int numDuplicates = 0;

		int temp = ary[ei];
		ary[ei] = ary[ri];
		ary[ri] = temp;
		ri = ei;
		ei--;

		while (si + numDuplicates <= ei) {
			if (ary[si + numDuplicates] == ary[ri])
				numDuplicates++;
			else if (ary[si + numDuplicates] < ary[ri]) {
				if (numDuplicates > 0) {
					temp = ary[si + numDuplicates];
					ary[si + numDuplicates] = ary[ri];
					ary[si] = temp;
				}
				si++;
			}
			else {
				temp = ary[ei];
				ary[ei] = ary[si + numDuplicates];
				ary[si + numDuplicates] = temp;
				ei--;
			}
		}

		temp = ary[si + numDuplicates];
		ary[si + numDuplicates] = ary[ri];
		ary[ri] = temp;

		pivot = si;
		nD = numDuplicates;

	}

	/*
	public static void Partition(int si, int ei) {
		// Stuff we did in class
		int[] d = new int[ary.length];
		for(int i = 0; i < ary.length; i++)
			if (i < si || i > ei)
				d[i] = ary[i];
		int ri = si + (int)(Math.random() * (ei - si + 1));
		int value = ary[ri];
		System.out.println("Pivot = " + value);
		int end = ei;
		for (ri = si; ri <= end; ri++) {
			if (ary[ri] < value) {
				d[si] = ary[ri];
				si++;
			}
			else if (ary[ri] > value) {
				d[ei] = ary[ri];
				ei--;
			}
		}
		d[si] = value;
		for (ri = 0; ri < ary.length; ri++)
			ary[ri] = d[ri];
	}
	*/

}
