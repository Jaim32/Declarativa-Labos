package org.example.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {
    private Theme() {}

    public static final Color BG = new Color(0x16171F);
    public static final Color CARD = new Color(0x20222D);
    public static final Color CARD_HOVER = new Color(0x272A38);
    public static final Color BORDER = new Color(0x323547);
    public static final Color TEXT = new Color(0xF3F4F9);
    public static final Color MUTED = new Color(0x8C92A8);

    public static final Color BLUE = new Color(0x5A82FF);
    public static final Color GREEN = new Color(0x3DDC97);
    public static final Color AMBER = new Color(0xFFB74D);
    public static final Color RED = new Color(0xFF6363);
    public static final Color PURPLE = new Color(0x9D4EDD);

    public static Font sans(int estilo, int tamano) {
        return new Font(Font.SANS_SERIF, estilo, tamano);
    }

    public static Font mono(int estilo, int tamano) {
        return new Font(Font.MONOSPACED, estilo, tamano);
    }
}
