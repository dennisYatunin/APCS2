import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Maze {

	private final static int BFS = 0;
	private final static int DFS = 1;
	private final static int BEST = 2;
	private final static int ASTAR = 3;

	private class Pos {
		private int row, col, numPrevious;
		private Pos previous;
		public Pos(int r, int c) {
			row = r;
			col = c;
		}
		public Pos(int r, int c, int n, Pos p) {
			row = r;
			col = c;
			numPrevious = n;
			previous = p;
		}
		public int row() {
			return row;
		}
		public int col() {
			return col;
		}
		public int numPrevious() {
			return numPrevious;
		}
		public Pos previous() {
			return previous;
		}
		public boolean equals(Pos p) {
			return row == p.row() && col == p.col();
		}
		public int distanceTo(Pos p) {
			return
			((row - p.row() >= 0) ? row - p.row() : p.row() - row) +
			((col - p.col() >= 0) ? col - p.col() : p.col() - col);
		}
	}

	private class Frontier {
		private Pos[] positions;
		private int[] stepCount;
		private int currentPos, endPos;
		private int mode;
		public Frontier(int capacity) {
			positions = new Pos[capacity];
			stepCount = new int[capacity];
		}
		public void setMode(int m) {
			mode = m;
		}
		public void add(Pos p) {
			positions[endPos++] = p;
		}
		public Pos remove() {
			int target;
			Pos result;
			switch (mode) {
				case BFS:
					return positions[currentPos++];
				case DFS:
					return positions[--endPos];
				case BEST:
					target = findMinBEST(end);
					result = positions[target];
					positions[target] = positions[--endPos];
					return result;
				case ASTAR:
					target = findMinASTAR(end);
					result = positions[target];
					positions[target] = positions[--endPos];
					return result;
			}
			return null;
		}
		public Pos peek() {
			if (mode == BFS)
				return positions[currentPos];
			// else if mode == DFS
			return positions[endPos - 1];
		}
		public int size() {
			return endPos - currentPos;
		}
		public void clear() {
			currentPos = endPos = 0;
		}
		public int findMinBEST(Pos p) {
			int result = currentPos;
			int distanceToResult = p.distanceTo(positions[currentPos]);
			for (int pointer = currentPos + 1; pointer < endPos; pointer++)
				if (p.distanceTo(positions[pointer]) < distanceToResult) {
					result = pointer;
					distanceToResult = p.distanceTo(positions[pointer]);
				}
			return result;
		}
		public int findMinASTAR(Pos p) {
			int result = currentPos;
			int distanceToResultPlusNumPrevious = p.distanceTo(positions[currentPos]) + positions[currentPos].numPrevious();
			for (int pointer = currentPos + 1; pointer < endPos; pointer++)
				if (p.distanceTo(positions[pointer]) + positions[pointer].numPrevious() < distanceToResultPlusNumPrevious) {
					result = pointer;
					distanceToResultPlusNumPrevious = p.distanceTo(positions[pointer]) + positions[pointer].numPrevious();
				}
			return result;
		}
	}

	private char[][] maze;
	private int maxx, maxy;
	private Pos start, end;
	private Frontier possiblePositions;
	private int[] solution;

	public Maze(String filename) {
		try {
			BufferedReader f = new BufferedReader(new FileReader(filename));String line;
			String file = f.readLine();
			maxx = file.length();
			maxy = 1;
			while (f.ready()) {
				line = f.readLine();
				if (line.equals(""))
					break;
				file += line;
				maxy++;
			}
			maze = new char[maxy][maxx];
			int emptyPositions = 1;
			char charHere;
			for(int i = 0; i < file.length(); i++) {
				charHere = file.charAt(i);
				maze[i / maxx][i % maxx] = charHere;
				if (charHere == ' ')
					emptyPositions++;
				else if(charHere == 'S')
					start = new Pos(i / maxx, i % maxx);
				else if(charHere == 'E')
					end = new Pos(i / maxx, i % maxx);
			}
			possiblePositions = new Frontier(emptyPositions);
			solution = new int[0];
		}
		catch(IOException e){
			System.out.println("ERROR: " + filename + " could not be opened.");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public String toString() {
		int r, c;
		String result = "";
		for (r = 0; r < maze.length; r++) {
			for (c = 0; c < maze[0].length; c++)
				result += maze[r][c];
			result += '\n';
		}
		return result;
	}

	public String toString(boolean animate) {
		if (animate) {
			String clear =  "\033[2J";
			String hide =  "\033[?25l";
			String show =  "\033[?25h";
			String go = "\033[0;0H";
			return hide + clear + go + toString() + "\n" + show;
		}
		return toString();
	}

	public boolean solve(boolean animate, int mode) {
		possiblePositions.setMode(mode);
		possiblePositions.clear();
		possiblePositions.add(start);
		Pos posHere = null;
		// edge cases for the win...
		while (possiblePositions.size() > 0) {
			posHere = possiblePositions.remove();
			maze[posHere.row()][posHere.col()] = 'x';
			if (posHere.col() < maxx - 1 && maze[posHere.row()][posHere.col() + 1] == ' ')
				possiblePositions.add(new Pos(posHere.row(), posHere.col() + 1, posHere.numPrevious() + 1, posHere));
			if (posHere.col() > 0 && maze[posHere.row()][posHere.col() - 1] == ' ')
				possiblePositions.add(new Pos(posHere.row(), posHere.col() - 1, posHere.numPrevious() + 1, posHere));
			if (posHere.row() < maxy - 1 && maze[posHere.row() + 1][posHere.col()] == ' ')
				possiblePositions.add(new Pos(posHere.row() + 1, posHere.col(), posHere.numPrevious() + 1, posHere));
			if (posHere.row() > 0 && maze[posHere.row() - 1][posHere.col()] == ' ')
				possiblePositions.add(new Pos(posHere.row() - 1, posHere.col(), posHere.numPrevious() + 1, posHere));
			if ((posHere.col() < maxx - 1 && maze[posHere.row()][posHere.col() + 1] == 'E') ||
				(posHere.col() > 0 && maze[posHere.row()][posHere.col() - 1] == 'E') ||
				(posHere.row() < maxy - 1 && maze[posHere.row() + 1][posHere.col()] == 'E') ||
				(posHere.row() > 0 && maze[posHere.row() - 1][posHere.col()] == 'E'))
				break;
			if (animate)
				System.out.println(toString(animate));
		}
		if (possiblePositions.size() == 0)
			return false;
		int r, c;
		for (r = 0; r < maze.length; r++)
			for (c = 0; c < maze[0].length; c++)
				if (maze[r][c] == 'x')
					maze[r][c] = ' ';
		boolean notSolvedBefore = false;
		if (solution.length == 0) {
			notSolvedBefore = true;
			solution = new int[posHere.numPrevious() * 2 + 4];
			solution[0] = start.row();
			solution[1] = start.col();
			solution[solution.length - 2] = end.row();
			solution[solution.length - 1] = end.col();
		}
		while (!posHere.equals(start)) {
			if (notSolvedBefore) {
				solution[posHere.numPrevious() * 2] = posHere.row();
				solution[posHere.numPrevious() * 2 + 1] = posHere.col();
			}
			maze[posHere.row()][posHere.col()] = 'P';
			posHere = posHere.previous();
		}
		maze[start.row()][start.col()] = 'S';
		return true;
	}

	public boolean solveBFS(boolean animate){
		return solve(animate, BFS);
	}

	public boolean solveDFS(boolean animate){
		return solve(animate, DFS);
	}

	public boolean solveBEST(boolean animate){
		return solve(animate, BEST);
	}

	public boolean solveASTAR(boolean animate){
		return solve(animate, ASTAR);
	}

	public boolean solveBFS(){
		return solveBFS(false);
	}

	public boolean solveDFS(){
		return solveDFS(false);
	}

	public boolean solveBEST(){
		return solveBEST(false);
	}

	public boolean solveASTAR(){
		return solveASTAR(false);
	}

	public int[] solutionCoordinates(){
		return solution;
	}

	public String solution() {
		String result = "[";
		int counter = 0;
		while (counter < solution.length - 2)
			result += "(" + solution[counter++] + "," + solution[counter++] + ")->";
		result += "(" + solution[counter++] + "," + solution[counter++] + ")]";
		return result;
	}

/*
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Proper Usage Is: java Maze \"filename\"");
			System.exit(1);
		}
		Maze maze = new Maze(args[0]);
		System.out.println(maze.solveBFS());
		System.out.println(maze.solution());
	}
*/

}
