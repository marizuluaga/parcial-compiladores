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

import eia.felinegraph.algo.DijkstraResult;
import eia.felinegraph.algo.DijkstraSolver;
import eia.felinegraph.algo.WeightedEdge;
import eia.felinegraph.io.Mission2Case;
import eia.felinegraph.io.Mission2Parser;


public final class Mission2Panel extends MissionPanelBase {

    private static final int DRAW_THRESHOLD = 60;

    private static final String SAMPLE = String.join("\n",
            "3",
            "2 1 0 1",
            "0 1 100",
            "3 3 2 0",
            "0 1 100",
            "0 2 200",
            "1 2 50",
            "2 0 0 1");

    private final GraphDrawPanel graphPanel = new GraphDrawPanel();
    private final JTextArea tooLargeMessage = GuiUtil.message("", Theme.TEXT_MUTED);
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);
    private final JLabel answerLabel = new JLabel(" ");

    private List<Mission2Case> cases = new ArrayList<>();
    private List<DijkstraResult> results = new ArrayList<>();

    public Mission2Panel() {
        super("Mision 2 - Recuperar las cuentas de Claude",
                "Nero left behind a network map. All weights are non-negative, which is exactly the "
                        + "condition that makes Dijkstra's algorithm correct and optimal.");
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
        cases = Mission2Parser.parse(rawInput);
        results = new ArrayList<>(cases.size());

        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Mission2Case c : cases) {
            k++;
            DijkstraResult r = DijkstraSolver.solve(c.n, c.edges, c.s, c.d);
            results.add(r);
            sb.append("Case #").append(k).append(": ");
            sb.append(r.reachable ? String.valueOf(r.cost) : "Nina is very sad");
            sb.append('\n');
        }
        return new SolveOutput(sb.toString().stripTrailing(), cases.size());
    }

    @Override
    protected void renderCase(int index) {
        Mission2Case c = cases.get(index);
        DijkstraResult r = results.get(index);

        answerLabel.setText("Caso #" + (index + 1) + ":  " + (r.reachable ? "costo = " + r.cost : "Nina is very sad"));

        if (c.n > DRAW_THRESHOLD) {
            tooLargeMessage.setText("Esta red tiene " + c.n + " nodos, por encima del limite de " + DRAW_THRESHOLD
                    + "-node visualization limit from Section 2.3, so the drawing was omitted. "
                    + "The numeric answer above is still exact.");
            cards.show(cardHost, "toolarge");
            return;
        }

        Set<Long> pathPairs = new HashSet<>();
        if (r.reachable) {
            for (int i = 0; i + 1 < r.path.size(); i++) {
                pathPairs.add(pairKey(r.path.get(i), r.path.get(i + 1)));
            }
        }

        List<GraphDrawPanel.EdgeView> views = new ArrayList<>(c.edges.size());
        for (WeightedEdge e : c.edges) {
            boolean onPath = pathPairs.contains(pairKey(e.from, e.to));
            views.add(new GraphDrawPanel.EdgeView(e.from, e.to, e.weight, onPath, Theme.TEAL));
        }
        graphPanel.setGraph(c.n, views, false, null);
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
