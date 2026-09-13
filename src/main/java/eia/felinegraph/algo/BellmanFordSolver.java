package eia.felinegraph.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public final class BellmanFordSolver {

    private BellmanFordSolver() {
    }

    public static BellmanFordResult solve(int n, List<WeightedEdge> directedEdges, int s) {
        if (s < 0 || s >= n) {
            throw new IllegalArgumentException("Start node out of range for N=" + n);
        }
        for (WeightedEdge e : directedEdges) {
            if (e.from < 0 || e.from >= n || e.to < 0 || e.to >= n) {
                throw new IllegalArgumentException("Passage references node outside 0.." + (n - 1));
            }
        }

        long[] dist = new long[n];
        java.util.Arrays.fill(dist, BellmanFordResult.NO_ROUTE);
        dist[s] = 0L;

        for (int round = 1; round <= n - 1; round++) {
            boolean changed = false;
            for (WeightedEdge e : directedEdges) {
                if (dist[e.from] == BellmanFordResult.NO_ROUTE) {
                    continue;
                }
                long candidate = dist[e.from] + e.weight;
                if (candidate > dist[e.to]) {
                    dist[e.to] = candidate;
                    changed = true;
                }
            }
            if (!changed) {
                break; 
            }
        }

        boolean[] flagged = new boolean[n];
        for (WeightedEdge e : directedEdges) {
            if (dist[e.from] == BellmanFordResult.NO_ROUTE) {
                continue;
            }
            if (dist[e.from] + e.weight > dist[e.to]) {
                flagged[e.to] = true;
            }
        }
        List<List<Integer>> adjacency = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adjacency.add(new ArrayList<>());
        }
        for (WeightedEdge e : directedEdges) {
            adjacency.get(e.from).add(e.to);
        }

        boolean[] unbounded = new boolean[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (flagged[i]) {
                unbounded[i] = true;
                queue.add(i);
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adjacency.get(u)) {
                if (!unbounded[v]) {
                    unbounded[v] = true;
                    queue.add(v);
                }
            }
        }

        return new BellmanFordResult(dist, unbounded);
    }
}
