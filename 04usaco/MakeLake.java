import java.io.*;
import java.util.*;

public class MakeLake {
  public static void main (String [] args) throws IOException {

    BufferedReader f = new BufferedReader(new FileReader("makelake.in"));
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("makelake.out")));
    StringTokenizer st = new StringTokenizer(f.readLine());

    int R = Integer.parseInt(st.nextToken());
    int C = Integer.parseInt(st.nextToken());
    int E = Integer.parseInt(st.nextToken());
    int N = Integer.parseInt(st.nextToken());

    int[][] elevations = new int[R][C];
    int r, c;
    for (r = 0; r < R; r++) {
      st = new StringTokenizer(f.readLine());
      for (c = 0; c < C; c++)
        elevations[r][c] = Integer.parseInt(st.nextToken());
    }

    String[] instructions = new String[N];
    for (r = 0; r < N; r++)
      instructions[r] = f.readLine();

    int row, column, depth, maxValue;
    for (String s : instructions) {
      st = new StringTokenizer(s);
      row = Integer.parseInt(st.nextToken()) - 1;
      column = Integer.parseInt(st.nextToken()) - 1;
      depth = Integer.parseInt(st.nextToken());
      maxValue = elevations[row][column];
      for (r = 0; r < 3; r++)
        for (c = 0; c < 3; c++) {
          if (elevations[row + r][column + c] > maxValue)
            maxValue = elevations[row + r][column + c];
        }
      maxValue -= depth;
      for (r = 0; r < 3; r++)
        for (c = 0; c < 3; c++) {
          if (elevations[row + r][column + c] > maxValue)
            elevations[row + r][column + c] = Math.max(maxValue, elevations[row + r][column + c] - depth);
        }
    }

    int answer = 0;
    for (r = 0; r < R; r++) {
      for (c = 0; c < C; c++)
        if (E - elevations[r][c] > 0)
          answer += E - elevations[r][c];
    }

    out.println(answer * 5184);

    out.close();
    System.exit(0);

  }
}
