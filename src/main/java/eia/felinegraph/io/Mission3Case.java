package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.List;

/** One parsed Mission 3 test case: node count, directed weighted edges, start and destination. */
public final class Mission3Case {
    public final int n;
    public final List<WeightedEdge> edges;
    public final int s;
    public final int d;

    public Mission3Case(int n, List<WeightedEdge> edges, int s, int d) {
        this.n = n;
        this.edges = edges;
        this.s = s;
        this.d = d;
    }
}
