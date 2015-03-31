import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BFS {

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

	private class Queue {
		private Pos[] positions;
		private int currentPos, endPos;
		public Queue(int capacity) {
			positions = new Pos[capacity];
			currentPos = endPos = 0;
		}
		public void add(Pos p) {
			positions[endPos] = p;
			endPos++;
		}
		public Pos poll() {
			currentPos++;
			return positions[currentPos - 1];
		}
		public Pos peek() {
			return positions[currentPos];
		}
	}

	private char[][] maze, copyOfMaze;
	private Pos start, end;
	private Queue possiblePositions;

	public BFS(String filename) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(filename));
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		while (f.ready()) {
			line = f.readLine();
			if (!line.equals(""))
				lines.add(line);
		}
		maze = new char[lines.size()][lines.get(0).length()];
		int r, c;
		int emptyPositions = 0;
		for (r = 0; r < maze.length; r++)
			for (c = 0; c < maze[0].length; c++) {
				maze[r][c] = lines.get(r).charAt(c);
				if (maze[r][c] == ' ')
					emptyPositions++;
				else if (maze[r][c] == 'S')
					start = new Pos(r, c);
				else if (maze[r][c] == 'E')
					end = new Pos(r, c);
			}
		possiblePositions = new Queue(emptyPositions + 2);
		possiblePositions.add(start);
	}

	public String maze() {
		int r, c;
		String result = "";
		for (r = 0; r < maze.length; r++) {
			for (c = 0; c < maze[0].length; c++)
				result += maze[r][c];
			result += '\n';
		}
		return result;
	}

	public String solve() {
		Pos currentPos= null;
		while (possiblePositions.peek() != null) {
			currentPos = possiblePositions.poll();
			maze[currentPos.row()][currentPos.col()] = '@';
			if (maze[currentPos.row()][currentPos.col() + 1] == ' ')
				possiblePositions.add(new Pos(currentPos.row(), currentPos.col() + 1, currentPos));
			if (maze[currentPos.row()][currentPos.col() - 1] == ' ')
				possiblePositions.add(new Pos(currentPos.row(), currentPos.col() - 1, currentPos));
			if (maze[currentPos.row() + 1][currentPos.col()] == ' ')
				possiblePositions.add(new Pos(currentPos.row() + 1, currentPos.col(), currentPos));
			if (maze[currentPos.row() - 1][currentPos.col()] == ' ')
				possiblePositions.add(new Pos(currentPos.row() - 1, currentPos.col(), currentPos));
			if (maze[currentPos.row()][currentPos.col() + 1] == 'E' ||
				maze[currentPos.row()][currentPos.col() - 1] == 'E' ||
				maze[currentPos.row() + 1][currentPos.col()] == 'E' ||
				maze[currentPos.row() - 1][currentPos.col()] == 'E')
				break;
			//System.out.println(maze());
		}
		if (possiblePositions.peek() == null)
			return "NO SOLUTION";
		int r, c;
		for (r = 0; r < maze.length; r++)
			for (c = 0; c < maze[0].length; c++)
				if (maze[r][c] == '@')
					maze[r][c] = ' ';
		while (!currentPos.equals(start)) {
			maze[currentPos.row()][currentPos.col()] = '@';
			currentPos = currentPos.previous();
		}
		maze[start.row()][start.col()] = 'S';
		return "SOLUTION:\n" + maze();
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Proper Usage Is: java BFS \"filename\"");
			System.exit(1);
		}
		BFS bfs = new BFS(args[0]);
		System.out.println(bfs.solve());
	}

}
