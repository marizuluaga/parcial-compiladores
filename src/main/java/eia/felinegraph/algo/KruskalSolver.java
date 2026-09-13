package eia.felinegraph.algo;

import java.util.ArrayList;
import java.util.List;

public final class KruskalSolver {

    private KruskalSolver() {
    }

    /**
     * @param n     NUMERO DE NODOS (0..n-1)
     * @param n
     
     */
    public static KruskalResult solve(int n, List<WeightedEdge> edges) {
        List<WeightedEdge> sorted = new ArrayList<>(edges);
        sorted.sort((a, b) -> Long.compare(a.weight, b.weight));

        UnionFind uf = new UnionFind(n);
        List<WeightedEdge> mst = new ArrayList<>();
        long total = 0L;

        for (WeightedEdge e : sorted) {
            if (e.from < 0 || e.from >= n || e.to < 0 || e.to >= n) {
                throw new IllegalArgumentException("Cable references node outside 0.." + (n - 1));
            }
            if (e.from == e.to) {
                continue; 
            }
            if (uf.union(e.from, e.to)) {
                mst.add(e);
                total += e.weight;
            }
        }

        boolean connected = n == 0 || uf.componentCount() == 1;
        return new KruskalResult(connected, connected ? total : -1L, connected ? mst : List.of());
    }
}
