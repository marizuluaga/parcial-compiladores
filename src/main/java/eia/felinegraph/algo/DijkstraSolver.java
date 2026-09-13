package eia.felinegraph.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public final class DijkstraSolver {

    private DijkstraSolver() {
    }

    /**
     * @param n     number of nodes, numbered 0..n-1
     * @param edges bidirectional edges; self-loops and duplicate pairs are accepted
     *              and simply relaxed like any other edge (duplicates never hurt
     *              correctness, they just offer the same cheaper-or-not choice twice)
     */
    public static DijkstraResult solve(int n, List<WeightedEdge> edges, int s, int d) {
        if (s < 0 || s >= n || d < 0 || d >= n) {
            throw new IllegalArgumentException("Start/destination out of range for N=" + n);
        }

        List<long[]>[] adjacency = buildAdjacency(n, edges);

        long[] dist = new long[n];
        int[] parent = new int[n];
        boolean[] settled = new boolean[n];
        Arrays.fill(dist, Long.MAX_VALUE / 4);
        Arrays.fill(parent, -1);
        dist[s] = 0L;

        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.add(new long[]{0L, s});

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            int u = (int) top[1];
            if (settled[u]) {
                continue;
            }
            settled[u] = true;
            if (u == d) {
                break;
            }
            for (long[] edge : adjacency[u]) {
                int v = (int) edge[0];
                long w = edge[1];
                if (settled[v]) {
                    continue;
                }
                long candidate = dist[u] + w;
                if (candidate < dist[v]) {
                    dist[v] = candidate;
                    parent[v] = u;
                    pq.add(new long[]{candidate, v});
                }
            }
        }

        if (dist[d] >= Long.MAX_VALUE / 4) {
            return DijkstraResult.unreachable();
        }

        List<Integer> path = new ArrayList<>();
        int cur = d;
        while (cur != -1) {
            path.add(cur);
            cur = parent[cur];
        }
        Collections.reverse(path);
        return DijkstraResult.of(dist[d], path);
    }

    @SuppressWarnings("unchecked")
    private static List<long[]>[] buildAdjacency(int n, List<WeightedEdge> edges) {
        List<long[]>[] adjacency = new List[n];
        for (int i = 0; i < n; i++) {
            adjacency[i] = new ArrayList<>();
        }
        for (WeightedEdge e : edges) {
            if (e.from < 0 || e.from >= n || e.to < 0 || e.to >= n) {
                throw new IllegalArgumentException("Edge references node outside 0.." + (n - 1));
            }
            adjacency[e.from].add(new long[]{e.to, e.weight});
            if (e.from != e.to) {
                adjacency[e.to].add(new long[]{e.from, e.weight});
            }
        }
        return adjacency;
    }
}
