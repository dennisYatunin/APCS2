public class Sorts {

	private static int[] ints;
	private static int[] copyOfInts;

	public static void mergesort(int[] theDress) {
		// #TheDress

		ints = theDress;
		copyOfInts = new int[ints.length];
		recursiveMergesort(0, ints.length);

	}

	private static void recursiveMergesort(int min, int max) {
		// sorts the range of ints from min to max (not inclusive)

		if (min < max - 1) {
			int middleOfArray = (max + min) / 2;
			recursiveMergesort(min, middleOfArray);
			recursiveMergesort(middleOfArray, max);
			mergeHalves(min, middleOfArray, max);
		}
		// if min == max - 1, then this range of ints only contains 1 int

	}

	private static void mergeHalves(int min, int mid, int max) {
		// merges the range of ints from min to mid (not inclusive) and
		// the range from mid to max (not inclusive)

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

		// if the second half has been exhausted, move the remainder of
		// the first half to the end of the sorted array
		while (firstHalfPos < mid) {
			ints[overallPos] = copyOfInts[firstHalfPos];
			firstHalfPos++;
			overallPos++;
		}

	}

}
