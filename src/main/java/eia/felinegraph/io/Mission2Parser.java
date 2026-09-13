package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses Mission 2 input: T test cases, each a bidirectional weighted network plus
 * a start and destination node. See Section 4 of the project statement.
 */
public final class Mission2Parser {

    private Mission2Parser() {
    }

    public static List<Mission2Case> parse(String input) {
        TokenScanner sc = new TokenScanner(input);
        int t = sc.nextInt("T, the number of test cases");
        if (t < 0) {
            throw new ParseException("T must not be negative.");
        }

        List<Mission2Case> cases = new ArrayList<>();
        for (int caseNo = 1; caseNo <= t; caseNo++) {
            int n = sc.nextInt("N (case #" + caseNo + ")");
            int c = sc.nextInt("C (case #" + caseNo + ")");
            int s = sc.nextInt("S (case #" + caseNo + ")");
            int d = sc.nextInt("D (case #" + caseNo + ")");

            sc.expectRange(n, 1, 10_000, "N in case #" + caseNo);
            sc.expectRange(c, 0, 100_000, "C in case #" + caseNo);
            sc.expectRange(s, 0, n - 1, "S in case #" + caseNo);
            sc.expectRange(d, 0, n - 1, "D in case #" + caseNo);

            List<WeightedEdge> edges = new ArrayList<>(c);
            for (int i = 0; i < c; i++) {
                int a = sc.nextInt("connection endpoint A (case #" + caseNo + ")");
                int b = sc.nextInt("connection endpoint B (case #" + caseNo + ")");
                long w = sc.nextLong("connection weight W (case #" + caseNo + ")");
                sc.expectRange(a, 0, n - 1, "connection endpoint A in case #" + caseNo);
                sc.expectRange(b, 0, n - 1, "connection endpoint B in case #" + caseNo);
                sc.expectRange(w, 0, 1_000_000, "connection weight W in case #" + caseNo);
                edges.add(new WeightedEdge(a, b, w));
            }

            cases.add(new Mission2Case(n, edges, s, d));
        }
        return cases;
    }
}
