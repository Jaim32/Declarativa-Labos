package org.example.ui;

import java.awt.Color;
import java.awt.Font;

public final class Theme {
    private Theme() {}

    public static final Color BG = new Color(0xF5F6F3);
    public static final Color CARD = Color.WHITE;
    public static final Color CARD_HOVER = new Color(0xF0F4F1);
    public static final Color BORDER = new Color(0xDEE4DE);
    public static final Color TEXT = new Color(0x1E2B26);
    public static final Color MUTED = new Color(0x69766E);

    public static final Color BLUE = new Color(0x236A5A);
    public static final Color GREEN = new Color(0x246F58);
    public static final Color AMBER = new Color(0xA96B24);
    public static final Color RED = new Color(0xB3443D);
    public static final Color PURPLE = new Color(0x5C746B);

    public static Font sans(int estilo, int tamano) {
        return new Font("Dialog", estilo, tamano);
    }

    public static Font mono(int estilo, int tamano) {
        return new Font(Font.MONOSPACED, estilo, tamano);
    }
}
