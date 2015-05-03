public class BSTree<E extends Comparable<E>> {

	private class BSTreeNode<E extends Comparable<E>> {
		private E data;
		private int duplicates;
		private BSTreeNode<E> left;
		private BSTreeNode<E> right;
		private BSTreeNode<E> parent;
		public BSTreeNode(E d) {
			data = d;
		}
		public String toString() {
			return data.toString();
		}
		public String duplicateString() {
			String result = "";
			for (int i = 0; i < duplicates; i++)
				result += data.toString() + " ";
			result += data.toString();
			return result;
		}
		public void setData(E d) {
			data = d;
		}
		public void addDuplicate() {
			duplicates++;
		}
		public void removeDuplicate() {
			duplicates--;
		}
		public void setDuplicates(int k) {
			duplicates = k;
		}
		public void setLeft(BSTreeNode<E> n) {
			left = n;
		}
		public void setRight(BSTreeNode<E> n) {
			right = n;
		}
		public void setParent(BSTreeNode<E> n) {
			parent = n;
		}
		public E getData() {
			return data;
		}
		public int getDuplicates() {
			return duplicates;
		}
		public BSTreeNode<E> getLeft() {
			return left;
		}
		public BSTreeNode<E> getRight() {
			return right;
		}
		public BSTreeNode<E> getParent() {
			return parent;
		}
	}

	private BSTreeNode<E> root;

	public boolean isEmpty() {
		return root == null;
	}

	public void add(E d) {
		root = add(root, new BSTreeNode<E>(d), null);
	}

	private BSTreeNode<E> add(BSTreeNode<E> curr, BSTreeNode<E> t, BSTreeNode<E> par) {
		if (curr == null) {
			curr = t;
			t.setParent(par);
		}
		else if (t.getData().compareTo(curr.getData()) < 0)
			curr.setLeft(add(curr.getLeft(), t, curr));
		else if (t.getData().compareTo(curr.getData()) > 0)
			curr.setRight(add(curr.getRight(), t, curr));
		else
			curr.addDuplicate();
		return curr;
	}

	public void remove(E d) {
		root = remove(root, d);
	}

	private BSTreeNode<E> remove(BSTreeNode<E> curr, E d ) {
		if (curr == null)
			return null;
		if (d.compareTo(curr.getData()) < 0)
			curr.setLeft(remove(curr.getLeft(), d));
		else if (d.compareTo(curr.getData()) > 0)
			curr.setRight(remove(curr.getRight(), d));
		else {
			if (curr.getDuplicates() > 0)
				curr.removeDuplicate();
			else if (curr.getLeft() == null && curr.getRight() == null)
				curr = null;
			else if (curr.getLeft() == null)
				curr = curr.getRight();
			else if (curr.getRight() == null)
				curr = curr.getLeft();
			else if (Math.random() < 0.5)
				extractMaxLeft(curr);
			else
				extractMinRight(curr);
		}
		return curr;
	}

	private void extractMaxLeft(BSTreeNode<E> t) {
		//replaces t with the right-most descendant
		//of the left descendant of t
		BSTreeNode<E> temp = t.getLeft();
		boolean wentRight = false;
		while (temp.getRight() != null) {
			wentRight = true;
			temp = temp.getRight();
		}
		t.setData(temp.getData());
		t.setDuplicates(temp.getDuplicates());
		System.out.println("temp = " + temp);
		if (wentRight)
			temp.getParent().setRight(temp.getLeft());
		else
			temp.getParent().setLeft(temp.getLeft());
	}

	private void extractMinRight(BSTreeNode<E> t) {
		//replaces t with the left-most descendant
		//of the right descendant of t
		BSTreeNode<E> temp = t.getRight();
		boolean wentLeft = false;
		while (temp.getLeft() != null) {
			wentLeft = true;
			temp = temp.getLeft();
		}
		t.setData(temp.getData());
		t.setDuplicates(temp.getDuplicates());
		System.out.println("temp = " + temp);
		if (wentLeft)
			temp.getParent().setLeft(temp.getRight());
		else
			temp.getParent().setRight(temp.getRight());
	}

	public void inOrder() {
		if (root != null)
			inOrder(root);
		System.out.println();
	}

	private void inOrder(BSTreeNode<E> curr) {
		if (curr.getLeft() != null)
			inOrder(curr.getLeft());
		System.out.print(curr.duplicateString() + " ");
		if (curr.getRight() != null)
			inOrder(curr.getRight());
	}

	public int getHeight() {
		if (root == null)
			return 0;
		return getHeight(root);
	}

	private int getHeight(BSTreeNode<E> curr) {
		int subtreeHeight = 0;
		if (curr.getLeft() != null)
			subtreeHeight = getHeight(curr.getLeft());
		if (curr.getRight() != null)
			if (getHeight(curr.getRight()) > subtreeHeight)
				subtreeHeight = getHeight(curr.getRight());
		subtreeHeight++;
		return subtreeHeight;
	}

	private int maxLength() {
		// returns the minimum number of characters required
		// to print the data from any node in the tree
		if (root == null)
			return 0;
		return maxLength(root);
	}

	private int maxLength(BSTreeNode<E> curr) {
		int max = curr.toString().length();
		int temp;
		if (curr.getLeft() != null) {
			temp = maxLength(curr.getLeft());
			if (temp > max)
				max = temp;
		}
		if (curr.getRight() != null) {
			temp = maxLength(curr.getRight());
			if (temp > max)
				max = temp;
		}
		return max;
	}

	private String spaces(double n) {
		// returns a String of n spaces
		if (n == 0)
			return "";
		return String.format("%" + (int)n + "s", "");
	}

	/*
		getLevel will produce a String for each level of the tree.
		The resulting Strings will look like this:

		._______________________________
		._______________._______________
		._______._______._______._______
		.___.___.___.___.___.___.___.___
		._._._._._._._._._._._._._._._._

		toString will combine those Strings and provide an output that
		will look like this:

		_______________.
		_______._______________.
		___._______._______._______.
		_.___.___.___.___.___.___.___.
		._._._._._._._._._._._._._._._.

		In these diagrams, each dot represents wordLength characters,
		each underscore represents wordLength spaces, and, for any nodes
		that are null, the dots will be "replaced" by underscores.
	*/

	private String getLevel(BSTreeNode<E> curr,
							int currLevel,
							int targetLevel,
							int height,
							int wordLength) {

		if (currLevel == 1)
			return
				curr.toString() +
				spaces(wordLength - curr.toString().length()) +
				spaces(
					wordLength *
					Math.pow(2, height - targetLevel + 1) -
					wordLength
				);

		String result = "";

		if (curr.getLeft() != null)
			result +=
				getLevel(
					curr.getLeft(),
					currLevel - 1,
					targetLevel,
					height,
					wordLength
				);

		else
			result +=
				spaces(
					wordLength *
					Math.pow(2, height - targetLevel + currLevel - 1)
				);

		if (curr.getRight() != null)
			result +=
				getLevel(
					curr.getRight(),
					currLevel - 1,
					targetLevel,
					height,
					wordLength
				);

		else
			result +=
				spaces(
					wordLength *
					Math.pow(2, height - targetLevel + currLevel - 1)
				);

		return result;

	}

	public String toString() {

		if (root == null)
			return "";
		String result = "";
		int height = getHeight();
		int wordLength = maxLength();

		// add the every level of the tree except the last one
		for (int level = 1; level < height; level++)
			result +=
				//add spaces to the front of each level's String
				//to keep everything centered
				spaces(
					wordLength * Math.pow(2, height - level) -
					wordLength
				) +
				getLevel(root, level, level, height, wordLength).
					//remove extra spaces from the end of each
					//level's String to prevent lines from
					//getting unnecessarily long
					replaceFirst("\\s+$", "") +
				"\n";

		// now add the last level
		result +=
			getLevel(root, height, height, height, wordLength).
				replaceFirst("\\s+$", "");

		return result;

	}

	public static void main(String[] args) {

		BSTree<Integer> t = new BSTree<Integer>();

		for (int i = 0; i < 15; i++)
			t.add((int)(Math.random() * 20));
		System.out.println("In-order: ");
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 1:");
		t.remove(1);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 3:");
		t.remove(3);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 5:");
		t.remove(5);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 7:");
		t.remove(7);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 9:");
		t.remove(9);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 11:");
		t.remove(11);
		t.inOrder();
		System.out.println(t);
		System.out.println("Removing 13:");
		t.remove(13);
		t.inOrder();
		System.out.println(t);

	}

}
