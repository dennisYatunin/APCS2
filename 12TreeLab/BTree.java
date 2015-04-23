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
			root.setData(d);
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
		if (mode == PRE_ORDER)
			preOrder(root);
		else if (mode == IN_ORDER)
			inOrder(root);
		else // if mode == POST_ORDER
			postOrder(root);
		System.out.println();
	}

	public void preOrder(TreeNode<E> curr) {
		System.out.print(curr + ((curr.getLeft() != null || curr.getRight() != null) ? ", " : ""));
		if (curr.getLeft() != null)
			preOrder(curr.getLeft());
		if (curr.getRight() != null)
			preOrder(curr.getRight());
	}

	public void inOrder(TreeNode<E> curr) {
		if (curr.getLeft() != null)
			preOrder(curr.getLeft());
		System.out.print(curr + ((curr.getLeft() != null || curr.getRight() != null) ? ", " : ""));
		if (curr.getRight() != null)
			preOrder(curr.getRight());
	}

	public void postOrder(TreeNode<E> curr) {
		if (curr.getLeft() != null)
			preOrder(curr.getLeft());
		if (curr.getRight() != null)
			preOrder(curr.getRight());
		System.out.print(curr + ((curr.getLeft() != null || curr.getRight() != null) ? ", " : ""));
	}

	public int getHeight() {
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

	public static void main(String[] args) {

		BTree<Integer> t = new BTree<Integer>();

		for (int i=0; i < 8; i++)
			t.add(i);
		System.out.println("Pre-order: ");
		t.traverse(PRE_ORDER);
		System.out.println("In-order: ");
		t.traverse(IN_ORDER);
		System.out.println("Post-order: ");
		t.traverse(POST_ORDER);
		System.out.println("Height: " + t.getHeight());

		//System.out.println(t);
	}

}
