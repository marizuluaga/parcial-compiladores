package eia.felinegraph.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public final class GuiUtil {

    private GuiUtil() {
    }

    public static JTextArea makeTextArea() {
        JTextArea area = new JTextArea();
        Theme.styleTextArea(area);
        area.setLineWrap(false);
        return area;
    }

    public static JScrollPane scroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.getViewport().setBackground(new Color(0x0f, 0x13, 0x17));
        sp.setBorder(BorderFactory.createLineBorder(Theme.GRID_LINE));
        return sp;
    }

    public static JComponent header(String title, String flavorText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.TITLE_FONT);
        titleLabel.setForeground(Theme.TEAL);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea flavor = new JTextArea(flavorText);
        flavor.setEditable(false);
        flavor.setOpaque(false);
        flavor.setLineWrap(true);
        flavor.setWrapStyleWord(true);
        flavor.setFocusable(false);
        flavor.setFont(Theme.FLAVOR_FONT);
        flavor.setForeground(Theme.TEXT_MUTED);
        flavor.setAlignmentX(Component.LEFT_ALIGNMENT);
        flavor.setBorder(new EmptyBorder(4, 0, 8, 0));

        panel.add(titleLabel);
        panel.add(flavor);
        return panel;
    }

    /** A wrapped, non-editable label used both for placeholders and "too large to draw" notices. */
    public static JTextArea message(String text, Color color) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFocusable(false);
        area.setFont(Theme.BODY_FONT);
        area.setForeground(color);
        area.setBorder(new EmptyBorder(16, 16, 16, 16));
        return area;
    }

    public static JPanel buttonRow(JButton... buttons) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row.setOpaque(false);
        for (JButton b : buttons) {
            row.add(b);
        }
        return row;
    }
}
