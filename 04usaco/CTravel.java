import java.io.*;
import java.util.*;

public class CTravel {
  public static void main (String [] args) throws IOException {

    BufferedReader f = new BufferedReader(new FileReader("ctravel.in"));
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ctravel.out")));
    StringTokenizer st = new StringTokenizer(f.readLine());

    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());
    int T = Integer.parseInt(st.nextToken());

    boolean[][] treeHere = new boolean[N][M];
    int r, c;
    String line;
    for (r = 0; r < N; r++) {
      line = f.readLine();
      for (c = 0; c < M; c++)
        if (line.charAt(c) == '*')
          treeHere[r][c] = true;
    }

    st = new StringTokenizer(f.readLine());
    int R1 = Integer.parseInt(st.nextToken()) - 1;
    int C1 = Integer.parseInt(st.nextToken()) - 1;
    int R2 = Integer.parseInt(st.nextToken()) - 1;
    int C2 = Integer.parseInt(st.nextToken()) - 1;

    int[][] ways = new int[N][M];
    ways[R1][C1] = 1;

    int[][] temp = new int[N][M];
    int timeLeft;
    for (timeLeft = T; timeLeft > 0; timeLeft--) {

      temp[0][0] = ways[0][1] + ways[1][0];
      for (c = 1; c < M - 1; c++)
        temp[0][c] = ways[0][c - 1] + ways[0][c + 1] + ways[1][c];
      temp[0][M - 1] = ways[0][M - 2] + ways[1][M - 1];
      for (r = 1; r < N - 1; r++) {
        temp[r][0] = ways[r][1] + ways[r - 1][0] + ways[r + 1][0];
        for (c = 1; c < M - 1; c++)
          temp[r][c] = ways[r][c - 1] + ways[r][c + 1] + ways[r - 1][c] + ways[r + 1][c];
        temp[r][M - 1] = ways[r][M - 2] + ways[r - 1][M - 1] + ways[r + 1][M - 1];
      }
      temp[N - 1][0] = ways[N - 1][1] + ways[N - 2][0];
      for (c = 1; c < M - 1; c++)
        temp[N - 1][c] = ways[N - 1][c - 1] + ways[N - 1][c + 1] + ways[N - 2][c];
      temp[N - 1][M - 1] = ways[N - 1][M - 2] + ways[N - 2][M - 1];

      for (r = 0; r < N; r++)
        for (c = 0; c < M; c++)
          if (!treeHere[r][c])
            ways[r][c] = temp[r][c];

      /*line = "";
      for (r = 0; r < N; r++) {
        for (c = 0; c < M; c++)
          line += ways[r][c] + " ";
        line += "\n";
      }
      System.out.println(line);*/

    }

    out.println(ways[R2][C2]);

    out.close();
    System.exit(0);

  }
}
