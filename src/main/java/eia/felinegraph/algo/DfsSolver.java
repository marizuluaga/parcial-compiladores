package eia.felinegraph.algo;

import java.util.ArrayList;
import java.util.List;

public final class DfsSolver {

    private static final int[] DR = {-1, 1, 0, 0}; 
    private static final int[] DC = {0, 0, -1, 1};

    private DfsSolver() {
    }

    public static GridPathResult solve(GridMaze maze, int sr, int sc, int dr, int dc) {
        if (!maze.inBounds(sr, sc) || !maze.inBounds(dr, dc)) {
            throw new IllegalArgumentException("Start or destination is outside the grid");
        }
        if (maze.isBomb(sr, sc) || maze.isBomb(dr, dc)) {
            return GridPathResult.unreachable();
        }
        if (sr == dr && sc == dc) {
            List<int[]> single = new ArrayList<>();
            single.add(new int[]{sr, sc});
            return GridPathResult.of(0, single);
        }

        int n = maze.rows * maze.cols;
        boolean[] visited = new boolean[n];
        int[] nextDir = new int[n]; 
        int[] stack = new int[n];
        int sp = 0;

        int startIdx = maze.index(sr, sc);
        int destIdx = maze.index(dr, dc);

        stack[sp++] = startIdx;
        visited[startIdx] = true;

        while (sp > 0) {
            int top = stack[sp - 1];
            if (top == destIdx) {
                List<int[]> path = new ArrayList<>(sp);
                for (int i = 0; i < sp; i++) {
                    path.add(new int[]{stack[i] / maze.cols, stack[i] % maze.cols});
                }
                return GridPathResult.of(sp - 1, path);
            }
            if (nextDir[top] < 4) {
                int d = nextDir[top]++;
                int topRow = top / maze.cols;
                int topCol = top % maze.cols;
                int nr = topRow + DR[d];
                int nc = topCol + DC[d];
                if (maze.inBounds(nr, nc) && !maze.isBomb(nr, nc)) {
                    int nIdx = maze.index(nr, nc);
                    if (!visited[nIdx]) {
                        visited[nIdx] = true;
                        stack[sp++] = nIdx;
                    }
                }
            } else {
                sp--; 
            }
        }

        return GridPathResult.unreachable();
    }
}
