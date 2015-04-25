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

	private String spaces(double n) {
		// returns a string of n spaces
		String result = "";
		for (int i = 0; i < n; i++)
			result += " ";
		return result;
	}

	private String getLevel(TreeNode<E> curr, int currLevel, int targetLevel, int height) {
		if (currLevel == 1)
			return curr.toString() + spaces(Math.pow(2, height - targetLevel + 1) - 1);
		String result = "";
		if (curr.getLeft() != null)
			result += getLevel(curr.getLeft(), currLevel - 1, targetLevel, height);
		else result += spaces(Math.pow(2, height - targetLevel + currLevel - 1));
		if (curr.getRight() != null)
			result += getLevel(curr.getRight(), currLevel - 1, targetLevel, height);
		else result += spaces(Math.pow(2, height - targetLevel + currLevel - 1));
		return result;
	}

	/*
		getLevel will produce Strings that look like:

		._______________________________
		._______________._______________
		._______._______._______._______
		.___.___.___.___.___.___.___.___
		._._._._._._._._._._._._._._._._

		toString will modify those levels to look like:

		_______________.
		_______._______________.
		___._______._______._______.
		_.___.___.___.___.___.___.___.
		._._._._._._._._._._._._._._._.
	*/

	public String toString() {
		String result = "";
		if (root != null) {
			int height = getHeight();
			for (int level = 1; level <= height; level++)
				result += spaces(Math.pow(2, height - level) - 1) +
					getLevel(root, level, level, height).replaceFirst("\\s+$", "") +
					"\n";
		}
		return result;
	}

	public static void main(String[] args) {

		BTree<Integer> t = new BTree<Integer>();

		for (int i = 0; i < 10; i++)
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
