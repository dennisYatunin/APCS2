import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;


public class KnightsTour{


	//instance variable
	private int[][]board;

	// for debugging
	public boolean debug;
	private JFrame world;
	private JTextPane content;


	public String name() {
		return "yatunin.dennis";
	}

	public KnightsTour(int size) {
		board = new int[size][size];
	}


	public KnightsTour(int size, boolean unbug){

		board = new int[size][size];

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
		int maxDigits = (int)Math.log10(board.length * board.length) + 1;
		int digitsHere;
		for (i = 0; i < board.length; i++) {
			for (j = 0; j < board.length; j++) {
				digitsHere = ((board[i][j] == 0) ? 0 : (int)Math.log10(board[i][j])) + 1;
				ans += " ";
				for (k = 0; k < maxDigits - digitsHere; k++)
					ans += "0";
				ans += board[i][j] + " ";
			}
			if (i != board.length - 1)
				ans += "\n\n";
		}
		return ans;
	}


	public boolean solve(){
		return solve(0, 0, 1);
	}


	public boolean solve(int x, int y){
		return solve(x, y, 1);
	}


	public boolean solve(int x, int y, int currentMoveNumber){

		if (debug) {
			content.setText(this.toString());
			wait(20);
		}

		if (currentMoveNumber == board.length * board.length + 1)
			return true;

		if (x < 0 || y < 0 || x >= board.length || y >= board.length)
			return false;

		if (board[y][x] == 0) {

			board[y][x] = currentMoveNumber;

			if (solve(x + 1, y + 2, currentMoveNumber + 1) ||
				solve(x + 1, y - 2, currentMoveNumber + 1) ||
				solve(x - 1, y + 2, currentMoveNumber + 1) ||
				solve(x - 1, y - 2, currentMoveNumber + 1) ||
				solve(x + 2, y + 1, currentMoveNumber + 1) ||
				solve(x + 2, y - 1, currentMoveNumber + 1) ||
				solve(x - 2, y + 1, currentMoveNumber + 1) ||
				solve(x - 2, y - 1, currentMoveNumber + 1))
				return true;

			board[y][x] = 0;

		}

		return false;

	}


	/*
	public static void main(String[] args) {
		KnightsTour k = new KnightsTour(10, true);
		if (k.solve())
			System.out.println(k);
	}
	*/


}
