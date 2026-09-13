package eia.felinegraph.algo;

import eia.felinegraph.io.Mission3Case;
import eia.felinegraph.io.Mission3Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Mission 3 tests: Floyd-Warshall and Bellman-Ford maximum-churun walks, using the
 * statement's own three sample cases, plus the required cross-check between them.
 */
class FloydWarshallBellmanFordTest {

    private static final String SAMPLE_INPUT = String.join("\n",
            "3",
            "5 7 0 4",
            "0 1 50",
            "0 2 10",
            "1 2 -30",
            "1 3 40",
            "2 1 -5",
            "2 3 60",
            "3 4 20",
            "4 4 0 3",
            "0 1 20",
            "1 2 30",
            "2 1 -10",
            "2 3 15",
            "3 3 0 2",
            "0 1 -40",
            "1 2 -25",
            "0 2 -80");

    @Test
    void sampleFromStatement_finiteMaximum() {
        Mission3Case c = Mission3Parser.parse(SAMPLE_INPUT).get(0);
        FloydWarshallResult fw = FloydWarshallSolver.solve(c.n, c.edges);
        assertFalse(fw.unbounded[c.s][c.d]);
        assertEquals(110L, fw.dist[c.s][c.d]);

        BellmanFordResult bf = BellmanFordSolver.solve(c.n, c.edges, c.s);
        assertTrue(bf.isReachable(c.d));
        assertFalse(bf.unbounded[c.d]);
        assertEquals(110L, bf.dist[c.d], "Bellman-Ford must agree with Floyd-Warshall for node D");
    }

    @Test
    void sampleFromStatement_positiveGainCycleIsUnbounded() {
        Mission3Case c = Mission3Parser.parse(SAMPLE_INPUT).get(1);
        FloydWarshallResult fw = FloydWarshallSolver.solve(c.n, c.edges);
        assertTrue(fw.dist[c.s][c.d] != FloydWarshallResult.NO_ROUTE, "D is reachable from S");
        assertTrue(fw.unbounded[c.s][c.d], "the 1<->2 cycle nets +20 per lap and can reach D");

        BellmanFordResult bf = BellmanFordSolver.solve(c.n, c.edges, c.s);
        assertTrue(bf.isReachable(c.d));
        assertTrue(bf.unbounded[c.d], "Bellman-Ford must independently detect the same unbounded node D");
    }

    @Test
    void sampleFromStatement_negativeMaximumIsStillReported() {
        Mission3Case c = Mission3Parser.parse(SAMPLE_INPUT).get(2);
        FloydWarshallResult fw = FloydWarshallSolver.solve(c.n, c.edges);
        assertFalse(fw.unbounded[c.s][c.d]);
        assertEquals(-65L, fw.dist[c.s][c.d], "max(-80 direct, -65 via node 1) is -65");

        BellmanFordResult bf = BellmanFordSolver.solve(c.n, c.edges, c.s);
        assertEquals(-65L, bf.dist[c.d]);
    }

    @Test
    void unreachableDestination_isNoRoute() {
        List<WeightedEdge> edges = List.of(new WeightedEdge(0, 1, 5));
        FloydWarshallResult fw = FloydWarshallSolver.solve(3, edges);
        assertEquals(FloydWarshallResult.NO_ROUTE, fw.dist[0][2]);
        assertFalse(fw.unbounded[0][2]);

        BellmanFordResult bf = BellmanFordSolver.solve(3, edges, 0);
        assertFalse(bf.isReachable(2));
    }

    @Test
    void pathReconstructionMatchesTheReportedMaximum() {
        Mission3Case c = Mission3Parser.parse(SAMPLE_INPUT).get(0);
        FloydWarshallResult fw = FloydWarshallSolver.solve(c.n, c.edges);
        List<Integer> path = FloydWarshallSolver.reconstructPath(fw, c.s, c.d);
        assertEquals(List.of(0, 1, 3, 4), path, "0->1->3->4 (50+40+20=110) is the expected optimal walk");
    }
}
