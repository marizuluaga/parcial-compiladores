package eia.felinegraph.io;

import eia.felinegraph.algo.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses Mission 3 input: T test cases, each a directed weighted network (possibly
 * with negative weights and cycles) plus a start and destination node. See
 * Section 5 of the project statement.
 */
public final class Mission3Parser {

    private Mission3Parser() {
    }

    public static List<Mission3Case> parse(String input) {
        TokenScanner sc = new TokenScanner(input);
        int t = sc.nextInt("T, the number of test cases");
        if (t < 0) {
            throw new ParseException("T must not be negative.");
        }

        List<Mission3Case> cases = new ArrayList<>();
        for (int caseNo = 1; caseNo <= t; caseNo++) {
            int n = sc.nextInt("N (case #" + caseNo + ")");
            int m = sc.nextInt("M (case #" + caseNo + ")");
            int s = sc.nextInt("S (case #" + caseNo + ")");
            int d = sc.nextInt("D (case #" + caseNo + ")");

            sc.expectRange(n, 1, 100, "N in case #" + caseNo);
            sc.expectRange(m, 0, 5000, "M in case #" + caseNo);
            sc.expectRange(s, 0, n - 1, "S in case #" + caseNo);
            sc.expectRange(d, 0, n - 1, "D in case #" + caseNo);

            List<WeightedEdge> edges = new ArrayList<>(m);
            for (int i = 0; i < m; i++) {
                int a = sc.nextInt("passage origin A (case #" + caseNo + ")");
                int b = sc.nextInt("passage destination B (case #" + caseNo + ")");
                long w = sc.nextLong("passage churun W (case #" + caseNo + ")");
                sc.expectRange(a, 0, n - 1, "passage origin A in case #" + caseNo);
                sc.expectRange(b, 0, n - 1, "passage destination B in case #" + caseNo);
                sc.expectRange(w, -1000, 1000, "passage churun W in case #" + caseNo);
                edges.add(new WeightedEdge(a, b, w));
            }

            cases.add(new Mission3Case(n, edges, s, d));
        }
        return cases;
    }
}
