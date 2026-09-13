package eia.felinegraph.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class BfsSolver {

    private static final int[] DR = {-1, 1, 0, 0}; 
    private static final int[] DC = {0, 0, -1, 1};

    private BfsSolver() {
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
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, -1);
        Arrays.fill(parent, -1);

        int startIdx = maze.index(sr, sc);
        int destIdx = maze.index(dr, dc);
        dist[startIdx] = 0;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(startIdx);

        while (!queue.isEmpty()) {
            int curIdx = queue.poll();
            if (curIdx == destIdx) {
                break;
            }
            int curRow = curIdx / maze.cols;
            int curCol = curIdx % maze.cols;
            for (int d = 0; d < 4; d++) {
                int nr = curRow + DR[d];
                int nc = curCol + DC[d];
                if (!maze.inBounds(nr, nc) || maze.isBomb(nr, nc)) {
                    continue;
                }
                int nIdx = maze.index(nr, nc);
                if (dist[nIdx] != -1) {
                    continue;
                }
                dist[nIdx] = dist[curIdx] + 1;
                parent[nIdx] = curIdx;
                queue.add(nIdx);
            }
        }

        if (dist[destIdx] == -1) {
            return GridPathResult.unreachable();
        }

        List<int[]> path = new ArrayList<>();
        int cur = destIdx;
        while (cur != -1) {
            path.add(new int[]{cur / maze.cols, cur % maze.cols});
            cur = parent[cur];
        }
        java.util.Collections.reverse(path);
        return GridPathResult.of(dist[destIdx], path);
    }
}
