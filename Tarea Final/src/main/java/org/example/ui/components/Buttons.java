package org.example.ui.components;

import org.example.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Buttons extends JButton {
    private final Color colorBase;
    private final Color colorTexto;
    private boolean hover;

    public Buttons(String texto, Color colorBase, Color colorTexto,
                   int padH, int padV, int tamFuente) {
        super(texto);
        this.colorBase = colorBase;
        this.colorTexto = colorTexto;

        setFont(Theme.sans(Font.BOLD, tamFuente));
        setForeground(colorTexto);
        setFocusPainted(true);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorder(new EmptyBorder(padV, padH, padV, padH));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color c = colorBase;
        if (!isEnabled()) {
            c = Theme.BORDER;
        } else if (getModel().isPressed()) {
            c = colorBase.darker();
        } else if (hover) {
            c = colorBase.equals(Theme.CARD) ? Theme.CARD_HOVER : colorBase.equals(Theme.GREEN) ? Theme.BLUE : colorBase.brighter();
        }

        g2.setColor(c);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        if (hasFocus() && isFocusPainted()) {
            g2.setColor(Theme.BLUE);
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 12, 12);
        }
        g2.dispose();

        super.paintComponent(g);
    }
}
