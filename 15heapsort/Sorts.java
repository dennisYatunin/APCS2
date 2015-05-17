public class Sorts {

	public static void heapsort(int[] ints) {
		// turn the array into a max heap
		heapify(ints);
		int temp;
		for (int ei = ints.length - 1; ei > 0; ei--) {
			// put the root of the heap at the front of
			// the sorted array
			temp = ints[ei];
			ints[ei] = ints[0];
			ints[0] = temp;
			// restore the heap
			pushDown(ints, 0, ei);
		}
	}

	public static void heapify(int[] ints) {
		// push down every int that lies to the left of
		// the middle of the array (the ones to the
		// right of the middle are all leaves)
		for (int si = ints.length / 2 - 1; si >= 0; si--)
			pushDown(ints, si, ints.length);
	}

	public static void pushDown(int[] ints, int si, int ei) {
		int max, temp;
		// keep looping as long as the int at si has a left child
		while (2 * si + 1 < ei) {
			// find the largest child of the int at si
			max = 2 * si + 1;
			if (2 * si + 2 < ei && ints[2 * si + 2] > ints[max])
				max = 2 * si + 2;
			// if necessary, swap the int at si with its largest child
			if (ints[max] > ints[si]) {
				temp = ints[max];
				ints[max] = ints[si];
				ints[si] = temp;
				// try to push the int at si further down
				si = max;
			}
			else
				return;
		}
	}

	public static void quicksort(int[] ints) {
		recursiveQuicksort(ints, 0, ints.length - 1);
	}

	private static void recursiveQuicksort(int[] ints, int si, int ei) {
		// pivot everything in the range of ints from si (inclusive)
		// to ei (inclusive) about some random index in that range

		if (si == ei || ei < si)
			return;

		int start = si;
		int ri = si + (int)(Math.random() * (ei - si + 1));
		int numDuplicates = 0;

		int temp = ints[ei];
		ints[ei] = ints[ri];
		ints[ri] = temp;
		ri = ei;
		ei--;

		while (si + numDuplicates <= ei) {
			if (ints[si + numDuplicates] == ints[ri])
				numDuplicates++;
			else if (ints[si + numDuplicates] < ints[ri]) {
				if (numDuplicates > 0) {
					temp = ints[si + numDuplicates];
					ints[si + numDuplicates] = ints[ri];
					ints[si] = temp;
				}
				si++;
			}
			else {
				temp = ints[ei];
				ints[ei] = ints[si + numDuplicates];
				ints[si + numDuplicates] = temp;
				ei--;
			}
		}

		temp = ints[si + numDuplicates];
		ints[si + numDuplicates] = ints[ri];
		ints[ri] = temp;

		recursiveQuicksort(ints, start, si - 1);
		recursiveQuicksort(ints, ei + 1, ri);

	}

	public static void mergesort(int[] ints) {
		recursiveMergesort(
			ints,
			new int[ints.length],
			0,
			ints.length
			);
	}

	private static void recursiveMergesort(
		int[] ints,
		int[] copyOfInts,
		int min,
		int max
		) {
		// sort the range of ints from min (inclusive)
		// to max (not inclusive)

		if (min < max - 1) {
			int middleOfArray = (max + min) / 2;
			recursiveMergesort(
				ints,
				copyOfInts,
				min,
				middleOfArray
				);
			recursiveMergesort(
				ints,
				copyOfInts,
				middleOfArray,
				max);
			mergeHalves(
				ints,
				copyOfInts,
				min,
				middleOfArray,
				max);
		}
		// if min == max - 1, then this range
		// of ints only contains one int

	}

	private static void mergeHalves(
		int[] ints,
		int[] copyOfInts,
		int min,
		int mid,
		int max
		) {
		// merge the range of ints from min (inclusive)
		// to mid (not inclusive) and the range from
		// mid (inclusive) to max (not inclusive)

		System.arraycopy(ints, min, copyOfInts, min, max - min);

		int overallPos = min;
		int firstHalfPos = min;
		int secondHalfPos = mid;

		while (firstHalfPos < mid && secondHalfPos < max) {
			if (copyOfInts[firstHalfPos] < copyOfInts[secondHalfPos]) {
				ints[overallPos] = copyOfInts[firstHalfPos];
				firstHalfPos++;
			}
			else {
				ints[overallPos] = copyOfInts[secondHalfPos];
				secondHalfPos++;
			}
			overallPos++;
		}

		// if the second half has been exhausted, move the remainder
		// of the first half to the end of the sorted array
		while (firstHalfPos < mid) {
			ints[overallPos] = copyOfInts[firstHalfPos];
			firstHalfPos++;
			overallPos++;
		}

	}

}
