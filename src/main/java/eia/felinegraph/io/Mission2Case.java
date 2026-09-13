package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.List;

/** One parsed Mission 2 test case: node count, bidirectional edges, start and destination. */
public final class Mission2Case {
    public final int n;
    public final List<WeightedEdge> edges;
    public final int s;
    public final int d;

    public Mission2Case(int n, List<WeightedEdge> edges, int s, int d) {
        this.n = n;
        this.edges = edges;
        this.s = s;
        this.d = d;
    }
}
