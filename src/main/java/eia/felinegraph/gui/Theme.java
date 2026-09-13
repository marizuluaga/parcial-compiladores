package eia.felinegraph.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public final class Theme {

    private Theme() {
    }

    public static final Color BG = new Color(0x14, 0x18, 0x1d);
    public static final Color PANEL_BG = new Color(0x1c, 0x22, 0x28);
    public static final Color PANEL_BG_LIGHT = new Color(0x23, 0x2b, 0x32);
    public static final Color TEXT = new Color(0xe9, 0xee, 0xf0);
    public static final Color TEXT_MUTED = new Color(0x9a, 0xa8, 0xae);
    public static final Color TEAL = new Color(0x1e, 0xb6, 0xa6);
    public static final Color TEAL_DARK = new Color(0x14, 0x7f, 0x76);
    public static final Color GOLD = new Color(0xe0, 0xa5, 0x3e);
    public static final Color RED = new Color(0xb0, 0x35, 0x35);
    public static final Color RED_BRIGHT = new Color(0xe0, 0x5a, 0x5a);
    public static final Color GRID_EMPTY = new Color(0x0d, 0x10, 0x14);
    public static final Color GRID_LINE = new Color(0x30, 0x38, 0x40);

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 15);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 13);
    public static final Font FLAVOR_FONT = new Font("SansSerif", Font.ITALIC, 12);

    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // fall back to the default cross-platform look and feel
        }
        UIManager.put("ToolTip.background", PANEL_BG_LIGHT);
        UIManager.put("ToolTip.foreground", TEXT);
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(TEAL);
        b.setForeground(Color.BLACK);
        b.setFont(HEADER_FONT.deriveFont(13f));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PANEL_BG_LIGHT);
        b.setForeground(TEXT);
        b.setFont(BODY_FONT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(GRID_LINE, 1));
        return b;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(HEADER_FONT);
        l.setForeground(TEAL);
        return l;
    }

    public static void styleTextArea(JTextArea area) {
        area.setBackground(new Color(0x0f, 0x13, 0x17));
        area.setForeground(TEXT);
        area.setCaretColor(TEAL);
        area.setFont(MONO_FONT);
        area.setBorder(new EmptyBorder(6, 8, 6, 8));
    }
}
