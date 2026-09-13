package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import eia.felinegraph.algo.KruskalResult;
import eia.felinegraph.algo.KruskalSolver;
import eia.felinegraph.algo.WeightedEdge;
import eia.felinegraph.io.Mission4Case;
import eia.felinegraph.io.Mission4Parser;


public final class Mission4Panel extends MissionPanelBase {

    private static final int DRAW_NODE_THRESHOLD = 100;
    private static final int DRAW_EDGE_THRESHOLD = 300;

    private static final String SAMPLE = String.join("\n",
            "1",
            "4",
            "5",
            "1 2 10",
            "2 3 20",
            "3 4 30",
            "4 1 40",
            "1 3 15");

    private final GraphDrawPanel graphPanel = new GraphDrawPanel();
    private final JTextArea tooLargeMessage = GuiUtil.message("", Theme.TEXT_MUTED);
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);
    private final JLabel answerLabel = new JLabel(" ");

    private List<Mission4Case> cases = new ArrayList<>();
    private List<KruskalResult> results = new ArrayList<>();

    public Mission4Panel() {
        super("Mision 4 - Reconectar la red",
                "Limon destroyed the campus cables. Kruskal's algorithm, backed by a union-find with "
                        + "path compression and union by size, greedily rebuilds the network for the least total cost.");
        initUi();
    }

    @Override
    protected JComponent buildVisualizationArea() {
        JPanel wrap = new JPanel(new BorderLayout(4, 4));
        wrap.setOpaque(false);

        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setOpaque(false);
        placeholder.add(GuiUtil.message("Load the sample or paste an input, then press Solve to see the network here.",
                Theme.TEXT_MUTED), BorderLayout.CENTER);

        JPanel tooLargeCard = new JPanel(new BorderLayout());
        tooLargeCard.setOpaque(false);
        tooLargeCard.add(tooLargeMessage, BorderLayout.CENTER);

        cardHost.setOpaque(false);
        cardHost.add(placeholder, "placeholder");
        cardHost.add(graphPanel, "graph");
        cardHost.add(tooLargeCard, "toolarge");
        cards.show(cardHost, "placeholder");

        answerLabel.setFont(Theme.HEADER_FONT);
        answerLabel.setForeground(Theme.TEAL);
        answerLabel.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        wrap.add(cardHost, BorderLayout.CENTER);
        wrap.add(answerLabel, BorderLayout.SOUTH);
        return wrap;
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected SolveOutput solveAll(String rawInput) {
        cases = Mission4Parser.parse(rawInput);
        results = new ArrayList<>(cases.size());

        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Mission4Case c : cases) {
            k++;
            KruskalResult r = KruskalSolver.solve(c.n, c.edges);
            results.add(r);
            sb.append("Case #").append(k).append(": ");
            sb.append(r.connected ? String.valueOf(r.totalCost) : "Limon cut too many cables");
            sb.append('\n');
        }
        return new SolveOutput(sb.toString().stripTrailing(), cases.size());
    }

    @Override
    protected void renderCase(int index) {
        Mission4Case c = cases.get(index);
        KruskalResult r = results.get(index);

        answerLabel.setText("Caso #" + (index + 1) + ":  "
                + (r.connected ? "costo MST = " + r.totalCost : "Limon cut too many cables"));

        if (c.n > DRAW_NODE_THRESHOLD || c.edges.size() > DRAW_EDGE_THRESHOLD) {
            tooLargeMessage.setText("Esta instancia tiene " + c.n + " intersecciones y " + c.edges.size()
                    + " cables, above the Section 2.3 visualization limit (" + DRAW_NODE_THRESHOLD
                    + " intersections / " + DRAW_EDGE_THRESHOLD + " cables), so the drawing was omitted. "
                    + "The numeric answer above is still exact.");
            cards.show(cardHost, "toolarge");
            return;
        }

        Set<Long> mstPairs = new HashSet<>();
        for (WeightedEdge e : r.mstEdges) {
            mstPairs.add(pairKey(e.from, e.to));
        }

        String[] labels = new String[c.n];
        for (int i = 0; i < c.n; i++) {
            labels[i] = String.valueOf(i + 1); // display with the statement's 1-indexed numbering
        }

        List<GraphDrawPanel.EdgeView> views = new ArrayList<>(c.edges.size());
        for (WeightedEdge e : c.edges) {
            boolean inMst = mstPairs.contains(pairKey(e.from, e.to));
            views.add(new GraphDrawPanel.EdgeView(e.from, e.to, e.weight, inMst, Theme.TEAL));
        }
        graphPanel.setGraph(c.n, views, false, labels);
        cards.show(cardHost, "graph");
    }

    private long pairKey(int a, int b) {
        int lo = Math.min(a, b), hi = Math.max(a, b);
        return ((long) lo << 32) | (hi & 0xffffffffL);
    }

    @Override
    protected void clearVisualization() {
        graphPanel.clear();
        answerLabel.setText(" ");
        cards.show(cardHost, "placeholder");
    }
}
