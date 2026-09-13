package eia.felinegraph.algo;

import java.util.ArrayList;
import java.util.List;


public final class FloydWarshallSolver {

    private FloydWarshallSolver() {
    }

    public static FloydWarshallResult solve(int n, List<WeightedEdge> directedEdges) {
        long[][] d = new long[n][n];
        int[][] next = new int[n][n];
        for (int i = 0; i < n; i++) {
            java.util.Arrays.fill(d[i], FloydWarshallResult.NO_ROUTE);
            java.util.Arrays.fill(next[i], -1);
            d[i][i] = 0L;
            next[i][i] = i;
        }

        for (WeightedEdge e : directedEdges) {
            if (e.from < 0 || e.from >= n || e.to < 0 || e.to >= n) {
                throw new IllegalArgumentException("Passage references node outside 0.." + (n - 1));
            }
            if (e.weight > d[e.from][e.to]) {
                d[e.from][e.to] = e.weight;
                next[e.from][e.to] = e.to;
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (d[i][k] == FloydWarshallResult.NO_ROUTE) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (d[k][j] == FloydWarshallResult.NO_ROUTE) {
                        continue;
                    }
                    long candidate = d[i][k] + d[k][j];
                    if (candidate > d[i][j]) {
                        d[i][j] = candidate;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        boolean[][] unbounded = new boolean[n][n];
        for (int k = 0; k < n; k++) {
            if (d[k][k] <= 0) {
                continue;
            }
            for (int i = 0; i < n; i++) {
                if (d[i][k] == FloydWarshallResult.NO_ROUTE) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (d[k][j] == FloydWarshallResult.NO_ROUTE) {
                        continue;
                    }
                    unbounded[i][j] = true;
                }
            }
        }

        return new FloydWarshallResult(d, unbounded, next);
    }

  
    public static List<Integer> reconstructPath(FloydWarshallResult result, int i, int j) {
        if (result.dist[i][j] == FloydWarshallResult.NO_ROUTE) {
            return null;
        }
        int n = result.next.length;
        List<Integer> path = new ArrayList<>();
        path.add(i);
        int cur = i;
        int guard = 0;
        do {
            cur = result.next[cur][j];
            if (cur == -1) {
                return null;
            }
            path.add(cur);
            guard++;
        } while (cur != j && guard <= n + 2);
        return path;
    }
}
