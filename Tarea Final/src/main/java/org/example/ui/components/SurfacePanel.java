package org.example.ui.components;

import org.example.ui.Theme;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SurfacePanel extends JPanel {
    private final Color fill;

    public SurfacePanel(int padding) {
        this(Theme.CARD, padding);
    }

    public SurfacePanel(Color fill, int padding) {
        this.fill = fill;
        setOpaque(false);
        setBorder(new EmptyBorder(padding, padding, padding, padding));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
        g2.dispose();
        super.paintComponent(g);
    }
}
