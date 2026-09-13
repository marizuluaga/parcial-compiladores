package eia.felinegraph.algo;

import eia.felinegraph.io.Mission4Case;
import eia.felinegraph.io.Mission4Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Mission 4 tests: Kruskal's MST over union-find, using the statement's own sample. */
class KruskalSolverTest {

    private static final String SAMPLE_INPUT = String.join("\n",
            "1",
            "4",
            "5",
            "1 2 10",
            "2 3 20",
            "3 4 30",
            "4 1 40",
            "1 3 15");

    @Test
    void sampleFromStatement_mstCostIs55() {
        Mission4Case c = Mission4Parser.parse(SAMPLE_INPUT).get(0);
        KruskalResult r = KruskalSolver.solve(c.n, c.edges);
        assertTrue(r.connected);
        assertEquals(55L, r.totalCost);
        assertEquals(3, r.mstEdges.size(), "an MST over 4 nodes has exactly 3 edges");
    }

    @Test
    void disconnectedGraph_reportsCannotReconnect() {
        // Nodes 0,1 connected; node 2 isolated -> the whole network cannot be reconnected.
        List<WeightedEdge> edges = List.of(new WeightedEdge(0, 1, 5));
        KruskalResult r = KruskalSolver.solve(3, edges);
        assertFalse(r.connected);
    }

    @Test
    void duplicateAndSelfLoopCablesDoNotCrash() {
        List<WeightedEdge> edges = List.of(
                new WeightedEdge(0, 0, 1),   // self-loop
                new WeightedEdge(0, 1, 10),
                new WeightedEdge(0, 1, 10),  // duplicate
                new WeightedEdge(1, 2, 5));
        KruskalResult r = KruskalSolver.solve(3, edges);
        assertTrue(r.connected);
        assertEquals(15L, r.totalCost);
    }

    @Test
    void unionFind_pathCompressionAndUnionBySize() {
        UnionFind uf = new UnionFind(6);
        assertTrue(uf.union(0, 1));
        assertTrue(uf.union(1, 2));
        assertFalse(uf.union(0, 2), "0 and 2 are already connected through 1");
        assertTrue(uf.connected(0, 2));
        assertFalse(uf.connected(0, 3));
        assertEquals(4, uf.componentCount()); // {0,1,2}, {3}, {4}, {5}
    }
}
