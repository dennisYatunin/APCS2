import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


public class NQueens{


	//instance variable
	private boolean[][]board;

	// for debugging
	public boolean debug;
	private JFrame world;
	private JTextPane content;


	public String name() {
		return "yatunin.dennis";
	}

	public NQueens(int size) {
		board = new boolean[size][size];
	}


	public NQueens(int size, boolean unbug){

		board = new boolean[size][size];

		if (unbug) {
			debug = unbug;
			world = new JFrame();
			content = new JTextPane();
			int screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDisplayMode().getHeight();
			Style style = content.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
			StyleConstants.setFontSize(style, screenHeight / 50);
			StyleConstants.setFontFamily(style, "Monospaced");
			world.setExtendedState(Frame.MAXIMIZED_BOTH);
			world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			world.add(content);
			world.pack();
			world.setVisible(true);
		}

	}


	public void wait(int millis){
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {}
	}


	public String toString(){
		String ans = "\n";
		int i, j, k;
		double maxDigitsMinusOne = Math.log10(board.length * board.length);
		for (i = 0; i < board.length; i++) {
			for (j = 0; j < board.length; j++)
				ans += (board[i][j]) ? "  Q  " : "  .  ";
			if (i != board.length - 1)
				ans += "\n\n";
		}
		return ans;
	}


	public boolean solve(){
		// tries to find a solution with a queen at (0, 0)
		return solve(0, 0);
	}


	public boolean solve(int x){
		// tries to find a solution with a queen at (x, 0)
		return solve(x, 0);
	}


	public boolean solve(int row, int col){

		if (debug) {
			content.setText(this.toString());
			wait(20);
		}

		if (col == board.length)
			return true;

		// place the queen here
		board[row][col] = true;

		int r, c;
		boolean cannotPlaceQueenHere;
		for (r = 0; r < board.length; r++) {

			cannotPlaceQueenHere = false;

			for (c = 0; c < col; c++) {

				// if there's another queen in this row
				if (board[row][c]) {
					cannotPlaceQueenHere = true;
					break;
				}

				// if there's another queen along the downward diagonal
				if (row + c - col >= 0 && board[row + c - col][c]) {
					cannotPlaceQueenHere = true;
					break;
				}

				// if there's another queen along the upward diagonal
				if (row - c + col < board.length && board[row - c + col][c]) {
					cannotPlaceQueenHere = true;
					break;
				}

			}

			if (!cannotPlaceQueenHere && solve(r, col + 1))
				return true;

		}

		// remove the queen from here
		board[row][col] = false;

		return false;

	}


	/*
	public static void main(String[] args) {
		NQueens n = new NQueens(10, true);
		if (n.solve())
			System.out.println(n);
	}
	*/


}
