public class BTree<E> {

	public static final int PRE_ORDER = 0;
	public static final int IN_ORDER = 1;
	public static final int POST_ORDER = 2;

	private class TreeNode<E> {
		private E data;
		private TreeNode<E> left;
		private TreeNode<E> right;
		public TreeNode(E d) {
			data = d;
		}
		public String toString() {
			return data.toString();
		}
		public void setData(E d) {
			data = d;
		}
		public void setLeft(TreeNode<E> n) {
			left = n;
		}
		public void setRight(TreeNode<E> n) {
			right = n;
		}
		public E getData() {
			return data;
		}
		public TreeNode<E> getLeft() {
			return left;
		}
		public TreeNode<E> getRight() {
			return right;
		}
		public TreeNode<E> getRand() {
			return (Math.random() < 0.5) ? left : right;
		}
	}

	private TreeNode<E> root;

	public void add(E d) {
		if (root == null)
			root = new TreeNode<E>(d);
		else add(root, new TreeNode<E>(d));
	}

	private void add(TreeNode<E> curr, TreeNode<E> bn) {
		if (curr.getLeft() == null)
			curr.setLeft(bn);
		else if (curr.getRight() == null)
			curr.setRight(bn);
		else add(curr.getRand(), bn);
	}

	public void traverse(int mode) {
		if (root != null) {
			if (mode == PRE_ORDER)
				preOrder(root);
			else if (mode == IN_ORDER)
				inOrder(root);
			else // if mode == POST_ORDER
				postOrder(root);
		}
		System.out.println();
	}

	private void preOrder(TreeNode<E> curr) {
		System.out.print(curr + " ");
		if (curr.getLeft() != null)
			preOrder(curr.getLeft());
		if (curr.getRight() != null)
			preOrder(curr.getRight());
	}

	private void inOrder(TreeNode<E> curr) {
		if (curr.getLeft() != null)
			inOrder(curr.getLeft());
		System.out.print(curr + " ");
		if (curr.getRight() != null)
			inOrder(curr.getRight());
	}

	private void postOrder(TreeNode<E> curr) {
		if (curr.getLeft() != null)
			postOrder(curr.getLeft());
		if (curr.getRight() != null)
			postOrder(curr.getRight());
		System.out.print(curr + " ");
	}

	public int getHeight() {
		if (root == null)
			return 0;
		return getHeight(root);
	}

	private int getHeight(TreeNode<E> curr) {
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

	private int maxLength(TreeNode<E> curr) {
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
		String result = "";
		for (int i = 0; i < n; i++)
			result += " ";
		return result;
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

	private String getLevel(TreeNode<E> curr, int currLevel, int targetLevel, int height, int wordLength) {
		if (currLevel == 1)
			return curr.toString() + spaces(wordLength - curr.toString().length()) +
				spaces(wordLength * Math.pow(2, height - targetLevel + 1) - wordLength);
		String result = "";
		if (curr.getLeft() != null)
			result += getLevel(curr.getLeft(), currLevel - 1, targetLevel, height, wordLength);
		else result += spaces(wordLength * Math.pow(2, height - targetLevel + currLevel - 1));
		if (curr.getRight() != null)
			result += getLevel(curr.getRight(), currLevel - 1, targetLevel, height, wordLength);
		else result += spaces(wordLength * Math.pow(2, height - targetLevel + currLevel - 1));
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
			// remove extra spaces from the end of each level's String to prevent lines from
			// getting unnecessarily long and add spaces to the front of each level's String
			// to keep everything centered
			result += spaces(wordLength * Math.pow(2, height - level) - wordLength) +
				getLevel(root, level, level, height, wordLength).replaceFirst("\\s+$", "") +
				"\n";
		// now add the last level (level = height)
		result += getLevel(root, height, height, height, wordLength).replaceFirst("\\s+$", "");
		return result;
	}

	public static void main(String[] args) {

		BTree<Integer> t = new BTree<Integer>();

		for (int i = 0; i < 20; i++)
			t.add(i);
		System.out.println("Pre-order: ");
		t.traverse(PRE_ORDER);
		System.out.println("In-order: ");
		t.traverse(IN_ORDER);
		System.out.println("Post-order: ");
		t.traverse(POST_ORDER);
		System.out.println("Height: " + t.getHeight());

		System.out.println(t);

	}

}
