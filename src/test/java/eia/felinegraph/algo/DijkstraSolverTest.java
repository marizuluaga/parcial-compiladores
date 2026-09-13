package eia.felinegraph.algo;

import eia.felinegraph.io.Mission2Case;
import eia.felinegraph.io.Mission2Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Mission 2 tests: Dijkstra's shortest path, using the statement's own three sample cases. */
class DijkstraSolverTest {

    private static final String SAMPLE_INPUT = String.join("\n",
            "3",
            "2 1 0 1",
            "0 1 100",
            "3 3 2 0",
            "0 1 100",
            "0 2 200",
            "1 2 50",
            "2 0 0 1");

    @Test
    void sampleFromStatement_allThreeCases() {
        List<Mission2Case> cases = Mission2Parser.parse(SAMPLE_INPUT);
        assertEquals(3, cases.size());

        DijkstraResult r1 = DijkstraSolver.solve(cases.get(0).n, cases.get(0).edges, cases.get(0).s, cases.get(0).d);
        assertTrue(r1.reachable);
        assertEquals(100L, r1.cost);

        DijkstraResult r2 = DijkstraSolver.solve(cases.get(1).n, cases.get(1).edges, cases.get(1).s, cases.get(1).d);
        assertTrue(r2.reachable);
        assertEquals(150L, r2.cost, "the two-hop route (50+100) beats the direct 200-cost edge");

        DijkstraResult r3 = DijkstraSolver.solve(cases.get(2).n, cases.get(2).edges, cases.get(2).s, cases.get(2).d);
        assertFalse(r3.reachable, "a graph with no edges at all must be unreachable");
    }

    @Test
    void startEqualsDestination_costIsZeroEvenWithNoEdges() {
        DijkstraResult r = DijkstraSolver.solve(5, List.of(), 3, 3);
        assertTrue(r.reachable);
        assertEquals(0L, r.cost);
    }

    @Test
    void selfLoopsAndDuplicateEdgesDoNotCrashOrChangeTheAnswer() {
        List<WeightedEdge> edges = List.of(
                new WeightedEdge(0, 0, 999), // self-loop
                new WeightedEdge(0, 1, 10),
                new WeightedEdge(0, 1, 10),  // duplicate of the same pair
                new WeightedEdge(1, 2, 5));
        DijkstraResult r = DijkstraSolver.solve(3, edges, 0, 2);
        assertTrue(r.reachable);
        assertEquals(15L, r.cost);
    }
}
