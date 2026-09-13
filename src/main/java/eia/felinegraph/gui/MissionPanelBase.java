package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import eia.felinegraph.io.ParseException;


public abstract class MissionPanelBase extends JPanel {

    protected final JTextArea inputArea = GuiUtil.makeTextArea();
    protected final JTextArea outputArea = GuiUtil.makeTextArea();
    protected final JComboBox<String> caseSelector = new JComboBox<>();
    protected final JLabel statusLabel = new JLabel(" ");

    protected MissionPanelBase(String title, String flavorText) {
        this.title = title;
        this.flavorText = flavorText;
        
    }

    private final String title;
    private final String flavorText;

   
    protected final void initUi() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(GuiUtil.header(title, flavorText), BorderLayout.NORTH);

        outputArea.setEditable(false);

        JButton loadSampleBtn = Theme.secondaryButton("Cargar ejemplo");
        JButton solveBtn = Theme.primaryButton("Resolver");
        JButton clearBtn = Theme.secondaryButton("Limpiar");
        loadSampleBtn.addActionListener(e -> inputArea.setText(getSampleInput()));
        solveBtn.addActionListener(e -> onSolve());
        clearBtn.addActionListener(e -> onClear());

        JPanel inputPanel = new JPanel(new BorderLayout(4, 4));
        inputPanel.setOpaque(false);
        inputPanel.add(Theme.sectionLabel("Entrada"), BorderLayout.NORTH);
        inputPanel.add(GuiUtil.scroll(inputArea), BorderLayout.CENTER);
        inputPanel.add(GuiUtil.buttonRow(loadSampleBtn, solveBtn, clearBtn), BorderLayout.SOUTH);

        JPanel outputPanel = new JPanel(new BorderLayout(4, 4));
        outputPanel.setOpaque(false);
        outputPanel.add(Theme.sectionLabel("Salida"), BorderLayout.NORTH);
        outputPanel.add(GuiUtil.scroll(outputArea), BorderLayout.CENTER);

        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, outputPanel);
        leftSplit.setResizeWeight(0.55);
        leftSplit.setBorder(null);
        leftSplit.setOpaque(false);

        JPanel right = new JPanel(new BorderLayout(4, 4));
        right.setOpaque(false);
        JPanel caseRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        caseRow.setOpaque(false);
        JLabel caseLabel = new JLabel("Caso de prueba:");
        caseLabel.setForeground(Theme.TEXT);
        caseRow.add(caseLabel);
        caseRow.add(caseSelector);
        right.add(Theme.sectionLabel("Visualizacion"), BorderLayout.NORTH);
        right.add(caseRow, BorderLayout.SOUTH);
        right.add(buildVisualizationArea(), BorderLayout.CENTER);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, right);
        mainSplit.setResizeWeight(0.42);
        mainSplit.setBorder(null);
        add(mainSplit, BorderLayout.CENTER);

        statusLabel.setForeground(Theme.TEXT_MUTED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 2, 0, 0));
        add(statusLabel, BorderLayout.SOUTH);

        caseSelector.addActionListener(e -> {
            int idx = caseSelector.getSelectedIndex();
            if (idx >= 0) {
                renderCase(idx);
            }
        });

        inputArea.setText(getSampleInput());
    }

    protected abstract JComponent buildVisualizationArea();

    protected abstract String getSampleInput();

   
    protected abstract SolveOutput solveAll(String rawInput);

    
    protected abstract void renderCase(int index);

    protected abstract void clearVisualization();

    private void onClear() {
        inputArea.setText("");
        outputArea.setText("");
        caseSelector.removeAllItems();
        clearVisualization();
        statusLabel.setText(" ");
        statusLabel.setForeground(Theme.TEXT_MUTED);
    }

    private void onSolve() {
        try {
            SolveOutput result = solveAll(inputArea.getText());
            outputArea.setText(result.outputText);
            outputArea.setCaretPosition(0);
            caseSelector.removeAllItems();
            for (int i = 1; i <= result.caseCount; i++) {
                caseSelector.addItem("Caso #" + i);
            }
            if (result.caseCount > 0) {
                caseSelector.setSelectedIndex(0);
            } else {
                clearVisualization();
            }
            statusLabel.setForeground(Theme.TEXT_MUTED);
            statusLabel.setText("Resueltos: " + result.caseCount + " caso(s) de prueba.");
        } catch (ParseException pe) {
            reportError("Entrada incorrecta: " + pe.getMessage());
        } catch (RuntimeException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            reportError("No fue posible resolver la entrada: " + msg);
        }
    }

    private void reportError(String message) {
        outputArea.setText(message);
        caseSelector.removeAllItems();
        clearVisualization();
        statusLabel.setForeground(Theme.RED_BRIGHT);
        statusLabel.setText("Corrige la entrada y presiona Resolver nuevamente.");
    }
}
