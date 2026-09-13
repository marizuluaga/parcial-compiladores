package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses Mission 4 input: T test cases, each a set of candidate bidirectional
 * cables between intersections numbered 1..N. Node ids are converted to 0-indexed
 * here so every algorithm in this project shares the same convention. See
 * Section 6 of the project statement.
 */
public final class Mission4Parser {

    private Mission4Parser() {
    }

    public static List<Mission4Case> parse(String input) {
        TokenScanner sc = new TokenScanner(input);
        int t = sc.nextInt("T, the number of test cases");
        if (t < 0) {
            throw new ParseException("T must not be negative.");
        }

        List<Mission4Case> cases = new ArrayList<>();
        for (int caseNo = 1; caseNo <= t; caseNo++) {
            int n = sc.nextInt("N (case #" + caseNo + ")");
            sc.expectRange(n, 1, 10_000, "N in case #" + caseNo);
            int c = sc.nextInt("C (case #" + caseNo + ")");
            sc.expectRange(c, 0, 100_000, "C in case #" + caseNo);

            List<WeightedEdge> edges = new ArrayList<>(c);
            for (int i = 0; i < c; i++) {
                int u = sc.nextInt("cable endpoint (case #" + caseNo + ")");
                int v = sc.nextInt("cable endpoint (case #" + caseNo + ")");
                long cost = sc.nextLong("cable cost (case #" + caseNo + ")");
                sc.expectRange(u, 1, n, "a cable endpoint in case #" + caseNo);
                sc.expectRange(v, 1, n, "a cable endpoint in case #" + caseNo);
                sc.expectRange(cost, 0, 1_000_000, "a cable cost in case #" + caseNo);
                edges.add(new WeightedEdge(u - 1, v - 1, cost));
            }

            cases.add(new Mission4Case(n, edges));
        }
        return cases;
    }
}
