package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import eia.felinegraph.algo.FloydWarshallResult;


public final class MatrixPanel extends JPanel {

    private final JTable table;
    private final JScrollPane scrollPane;
    private int s = -1, d = -1;

    public MatrixPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.PANEL_BG);
        table = new JTable();
        table.setBackground(new Color(0x0f, 0x13, 0x17));
        table.setForeground(Theme.TEXT);
        table.setGridColor(Theme.GRID_LINE);
        table.setFont(Theme.MONO_FONT.deriveFont(12f));
        table.setRowHeight(22);
        table.getTableHeader().setBackground(Theme.PANEL_BG_LIGHT);
        table.getTableHeader().setForeground(Theme.TEAL);
        table.setDefaultRenderer(Object.class, new HighlightRenderer());
        table.setCellSelectionEnabled(false);
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(0x0f, 0x13, 0x17));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setMatrix(FloydWarshallResult result, int n, int s, int d) {
        this.s = s;
        this.d = d;
        String[] columns = new String[n + 1];
        columns[0] = "i \\ j";
        for (int j = 0; j < n; j++) {
            columns[j + 1] = String.valueOf(j);
        }
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (int i = 0; i < n; i++) {
            Object[] row = new Object[n + 1];
            row[0] = String.valueOf(i);
            for (int j = 0; j < n; j++) {
                if (result.unbounded[i][j]) {
                    row[j + 1] = "inf";
                } else if (result.dist[i][j] == FloydWarshallResult.NO_ROUTE) {
                    row[j + 1] = "-";
                } else {
                    row[j + 1] = String.valueOf(result.dist[i][j]);
                }
            }
            model.addRow(row);
        }
        table.setModel(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        for (int j = 1; j <= n; j++) {
            table.getColumnModel().getColumn(j).setPreferredWidth(56);
        }
    }

    public void clear() {
        table.setModel(new DefaultTableModel());
        s = -1;
        d = -1;
    }

    private final class HighlightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(t, value, false, false, row, column);
            setHorizontalAlignment(CENTER);
            boolean isHeaderColumn = column == 0;
            boolean isTargetCell = !isHeaderColumn && row == s && (column - 1) == d;
            if (isTargetCell) {
                c.setBackground(Theme.TEAL);
                c.setForeground(Color.BLACK);
            } else if (isHeaderColumn) {
                c.setBackground(Theme.PANEL_BG_LIGHT);
                c.setForeground(Theme.TEAL);
            } else if ("inf".equals(value)) {
                c.setBackground(new Color(0x2a, 0x22, 0x14));
                c.setForeground(Theme.GOLD);
            } else {
                c.setBackground(new Color(0x0f, 0x13, 0x17));
                c.setForeground(Theme.TEXT);
            }
            return c;
        }
    }
}
