import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Maze {

	private final static int BFS = 0;
	private final static int DFS = 1;

	private class Pos {
		private int row, col;
		private Pos previous;
		public Pos(int r, int c) {
			row = r;
			col = c;
		}
		public Pos(int r, int c, Pos p) {
			row = r;
			col = c;
			previous = p;
		}
		public int row() {
			return row;
		}
		public int col() {
			return col;
		}
		public Pos previous() {
			return previous;
		}
		public boolean equals(Pos p) {
			return row == p.row() && col == p.col();
		}
	}

	private class Frontier {
		private Pos[] positions;
		private int currentPos, endPos;
		private int mode;
		public Frontier(int capacity) {
			positions = new Pos[capacity];
			currentPos = endPos = 0;
		}
		public void setMode(int m) {
			mode = m;
		}
		public void add(Pos p) {
			positions[endPos] = p;
			endPos++;
		}
		public Pos remove() {
			if (mode == BFS) {
				currentPos++;
				return positions[currentPos - 1];
			}
			// else if mode == DFS
			endPos--;
			return positions[endPos + 1];
		}
		public Pos peek() {
			if (mode == BFS)
				return positions[currentPos];
			// else if mode == DFS
			return positions[endPos];
		}
	}

	private char[][] maze;
	private int maxx, maxy;
	private Pos start;
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
			int emptyPositions = 0;
			char charHere;
			for(int i = 0; i < file.length(); i++) {
				charHere = file.charAt(i);
				maze[i / maxx][i % maxx] = charHere;
				if (charHere == ' ')
					emptyPositions++;
				if(charHere == 'S'){
					start = new Pos(i / maxx, i % maxx);
					emptyPositions++;
				}
			}
			possiblePositions = new Frontier(emptyPositions);
			possiblePositions.add(start);
			System.out.println(this);
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

	public String solve(boolean animate, int mode) {
		possiblePositions.setMode(mode);
		Pos posHere = null;
		while (possiblePositions.peek() != null) {
			posHere = possiblePositions.remove();
			maze[posHere.row()][posHere.col()] = 'x';
			if (posHere.col() < maxx - 1 && maze[posHere.row()][posHere.col() + 1] == ' ')
				possiblePositions.add(new Pos(posHere.row(), posHere.col() + 1, posHere));
			if (posHere.col() > 0 && maze[posHere.row()][posHere.col() - 1] == ' ')
				possiblePositions.add(new Pos(posHere.row(), posHere.col() - 1, posHere));
			if (posHere.row() < maxy - 1 && maze[posHere.row() + 1][posHere.col()] == ' ')
				possiblePositions.add(new Pos(posHere.row() + 1, posHere.col(), posHere));
			if (posHere.row() > 0 && maze[posHere.row() - 1][posHere.col()] == ' ')
				possiblePositions.add(new Pos(posHere.row() - 1, posHere.col(), posHere));
			if ((posHere.col() < maxx - 1 && maze[posHere.row()][posHere.col() + 1] == 'E') ||
				(posHere.col() > 0 && maze[posHere.row()][posHere.col() - 1] == 'E') ||
				(posHere.row() < maxy - 1 && maze[posHere.row() + 1][posHere.col()] == 'E') ||
				(posHere.row() > 0 && maze[posHere.row() - 1][posHere.col()] == 'E'))
				break;
			if (animate)
				System.out.println(toString());
		}
		if (possiblePositions.peek() == null) {
			if (solution == null)
				solution = new int[0];
			return "NO SOLUTION";
		}
		int r, c;
		for (r = 0; r < maze.length; r++)
			for (c = 0; c < maze[0].length; c++)
				if (maze[r][c] == 'x')
					maze[r][c] = ' ';
		while (!posHere.equals(start)) {
			maze[posHere.row()][posHere.col()] = 'x';
			posHere = posHere.previous();
		}
		maze[start.row()][start.col()] = 'S';
		return "SOLUTION:\n" + toString();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Proper Usage Is: java Maze \"filename\"");
			System.exit(1);
		}
		Maze maze = new Maze(args[0]);
		System.out.println(maze.solve(false, BFS));
	}

}
