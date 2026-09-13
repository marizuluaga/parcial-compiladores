package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.List;

/**
 * One parsed Mission 4 test case: intersection count and candidate cables. Node
 * ids here are already converted to the 0-indexed convention used by the algo
 * package (the statement numbers intersections 1..N).
 */
public final class Mission4Case {
    public final int n;
    public final List<WeightedEdge> edges;

    public Mission4Case(int n, List<WeightedEdge> edges) {
        this.n = n;
        this.edges = edges;
    }
}
