package org.example.ui.components;

import org.example.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class PanelGrafico extends JPanel {
    private String funcionStr;
    private Double limiteA;
    private Double limiteB;

    public PanelGrafico() {
        setPreferredSize(new Dimension(480, 250));
        setMinimumSize(new Dimension(300, 200));
        setBackground(new Color(0xFAFCFA));
    }

    public void actualizarGrafico(String funcion, Double a, Double b) {
        this.funcionStr = funcion;
        this.limiteA = a;
        this.limiteB = b;
        repaint();
    }

    public void limpiar() {
        this.funcionStr = null;
        this.limiteA = null;
        this.limiteB = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Fondo del gráfico
        g2.setColor(new Color(0xFAFCFA));
        g2.fillRect(0, 0, w, h);

        if (funcionStr == null || funcionStr.trim().isEmpty()) {
            g2.setColor(new Color(0xE8EEE9));
            int midX = w / 2, midY = h / 2 - 12;
            for (int i = -3; i <= 3; i++) {
                g2.drawLine(26, midY + i * 28, w - 26, midY + i * 28);
            }
            for (int i = -7; i <= 7; i++) {
                int x = midX + i * 36;
                if (x > 20 && x < w - 20) g2.drawLine(x, 20, x, h - 52);
            }
            g2.setColor(new Color(0xB8D4C5));
            g2.setStroke(new BasicStroke(2.5f));
            java.awt.geom.GeneralPath preview = new java.awt.geom.GeneralPath();
            for (int x = 26; x <= w - 26; x++) {
                double t = (x - midX) / 75.0;
                double y = midY - Math.sin(t) * 34 - t * 9;
                if (x == 26) preview.moveTo(x, y); else preview.lineTo(x, y);
            }
            g2.draw(preview);
            g2.setColor(Theme.MUTED);
            g2.setFont(Theme.sans(Font.PLAIN, 13));
            String msg = "La gráfica aparecerá después del cálculo";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h - 22);
            g2.dispose();
            return;
        }

        // Determinar rango de graficación [xMin, xMax]
        double xMin = -4.0;
        double xMax = 4.0;
        if (limiteA != null && limiteB != null) {
            double menor = Math.min(limiteA, limiteB);
            double mayor = Math.max(limiteA, limiteB);
            double span = Math.max(1.0, mayor - menor);
            xMin = menor - span * 0.4;
            xMax = mayor + span * 0.4;
        }

        // Muestrear valores de y para determinar rango [yMin, yMax]
        int pasos = 200;
        double dx = (xMax - xMin) / pasos;
        double yMin = -2.0;
        double yMax = 2.0;
        boolean primero = true;

        for (int i = 0; i <= pasos; i++) {
            double xVal = xMin + i * dx;
            double yVal = evaluarFuncion(funcionStr, xVal);
            if (!Double.isNaN(yVal) && !Double.isInfinite(yVal)) {
                if (primero) {
                    yMin = yVal;
                    yMax = yVal;
                    primero = false;
                } else {
                    yMin = Math.min(yMin, yVal);
                    yMax = Math.max(yMax, yVal);
                }
            }
        }

        // Asegurar margen vertical y que incluya el eje X (y = 0)
        yMin = Math.min(yMin, 0.0);
        yMax = Math.max(yMax, 0.0);
        double ySpan = Math.max(1.0, yMax - yMin);
        yMin -= ySpan * 0.2;
        yMax += ySpan * 0.2;

        // Transformación de coordenadas del plano a píxeles
        int pad = 34;
        double anchoGrafico = w - 2 * pad;
        double altoGrafico = h - 2 * pad;

        double scaleX = anchoGrafico / (xMax - xMin);
        double scaleY = altoGrafico / (yMax - yMin);

        // Origen en píxeles
        int originX = (int) (pad - xMin * scaleX);
        int originY = (int) (pad + yMax * scaleY);

        // 1. Dibujar Rejilla sutil
        g2.setColor(new Color(0xE9EFEB));
        g2.setStroke(new BasicStroke(1f));
        for (int i = 0; i <= 6; i++) {
            int gx = pad + (w - 2 * pad) * i / 6;
            g2.drawLine(gx, pad, gx, h - pad);
        }
        for (int i = 0; i <= 4; i++) {
            int gy = pad + (h - 2 * pad) * i / 4;
            g2.drawLine(pad, gy, w - pad, gy);
        }

        // 2. Dibujar Ejes X e Y
        g2.setColor(Theme.MUTED);
        g2.setStroke(new BasicStroke(1.2f));
        // Eje X (y = 0)
        if (originY >= pad && originY <= h - pad) {
            g2.drawLine(pad, originY, w - pad, originY);
        }
        // Eje Y (x = 0)
        if (originX >= pad && originX <= w - pad) {
            g2.drawLine(originX, pad, originX, h - pad);
        }

        // 3. Sombrear el Área bajo la curva entre a y b
        if (limiteA != null && limiteB != null) {
            double aVal = Math.min(limiteA, limiteB);
            double bVal = Math.max(limiteA, limiteB);

            GeneralPath areaPath = new GeneralPath();
            int pxA = (int) (pad + (aVal - xMin) * scaleX);
            int pxB = (int) (pad + (bVal - xMin) * scaleX);
            int yCero = Math.max(pad, Math.min(h - pad, originY));

            areaPath.moveTo(pxA, yCero);

            int subPasos = 100;
            double dSub = (bVal - aVal) / subPasos;
            for (int i = 0; i <= subPasos; i++) {
                double currX = aVal + i * dSub;
                double currY = evaluarFuncion(funcionStr, currX);
                if (Double.isNaN(currY) || Double.isInfinite(currY)) currY = 0;
                int py = (int) (pad + (yMax - currY) * scaleY);
                py = Math.max(pad, Math.min(h - pad, py));
                int px = (int) (pad + (currX - xMin) * scaleX);
                areaPath.lineTo(px, py);
            }
            areaPath.lineTo(pxB, yCero);
            areaPath.closePath();

            // Relleno verde translúcido
            g2.setColor(new Color(36, 111, 88, 42));
            g2.fill(areaPath);

            // Líneas verticales punteadas de los límites a y b
            Stroke dashed = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f}, 0.0f);
            g2.setStroke(dashed);
            g2.setColor(new Color(0x85A997));
            g2.drawLine(pxA, pad, pxA, h - pad);
            g2.drawLine(pxB, pad, pxB, h - pad);

            // Etiquetas a y b
            g2.setFont(Theme.sans(Font.BOLD, 11));
            g2.drawString(String.format("a=%.2f", aVal), pxA + 4, pad + 14);
            g2.drawString(String.format("b=%.2f", bVal), pxB + 4, pad + 28);
        }

        // 4. Dibujar la Curva f(x)
        GeneralPath curvaPath = new GeneralPath();
        boolean iniciado = false;
        for (int i = 0; i <= pasos; i++) {
            double xVal = xMin + i * dx;
            double yVal = evaluarFuncion(funcionStr, xVal);
            if (Double.isNaN(yVal) || Double.isInfinite(yVal)) {
                iniciado = false;
                continue;
            }
            int px = (int) (pad + (xVal - xMin) * scaleX);
            int py = (int) (pad + (yMax - yVal) * scaleY);

            // Evitar saltos de asíntotas
            if (py < -h || py > 2 * h) {
                iniciado = false;
                continue;
            }

            if (!iniciado) {
                curvaPath.moveTo(px, py);
                iniciado = true;
            } else {
                curvaPath.lineTo(px, py);
            }
        }

        g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Theme.GREEN);
        g2.draw(curvaPath);

        // Leyenda
        g2.setFont(Theme.sans(Font.BOLD, 12));
        g2.setColor(Theme.TEXT);
        g2.drawString("f(x) = " + funcionStr, pad, pad - 8);

        g2.dispose();
    }

    /**
     * Evaluador numérico simple y rápido para graficación continua en pantalla.
     */
    private double evaluarFuncion(String expr, double x) {
        expr = expr.replaceAll("\\s+", "").toLowerCase();
        try {
            return evalSub(expr, x);
        } catch (Exception ex) {
            return Double.NaN;
        }
    }

    private double evalSub(String expr, double x) {
        if (expr.isEmpty()) return 0;

        // Suma y Resta exterior (respetando paréntesis)
        int nivel = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') nivel++;
            else if (c == '(') nivel--;
            else if (nivel == 0 && (c == '+' || c == '-') && i > 0 && expr.charAt(i - 1) != '*' && expr.charAt(i - 1) != '/' && expr.charAt(i - 1) != '^') {
                String izq = expr.substring(0, i);
                String der = expr.substring(i + 1);
                return c == '+' ? evalSub(izq, x) + evalSub(der, x) : evalSub(izq, x) - evalSub(der, x);
            }
        }

        // Multiplicación y División
        nivel = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') nivel++;
            else if (c == '(') nivel--;
            else if (nivel == 0 && (c == '*' || c == '/')) {
                String izq = expr.substring(0, i);
                String der = expr.substring(i + 1);
                return c == '*' ? evalSub(izq, x) * evalSub(der, x) : evalSub(izq, x) / evalSub(der, x);
            }
        }

        // Potencia ^
        nivel = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') nivel++;
            else if (c == ')') nivel--;
            else if (nivel == 0 && c == '^') {
                String izq = expr.substring(0, i);
                String der = expr.substring(i + 1);
                return Math.pow(evalSub(izq, x), evalSub(der, x));
            }
        }

        // Paréntesis externos
        if (expr.startsWith("(") && expr.endsWith(")")) {
            return evalSub(expr.substring(1, expr.length() - 1), x);
        }

        // Funciones elementales
        if (expr.startsWith("sin(") && expr.endsWith(")")) return Math.sin(evalSub(expr.substring(4, expr.length() - 1), x));
        if (expr.startsWith("cos(") && expr.endsWith(")")) return Math.cos(evalSub(expr.substring(4, expr.length() - 1), x));
        if (expr.startsWith("tan(") && expr.endsWith(")")) return Math.tan(evalSub(expr.substring(4, expr.length() - 1), x));
        if (expr.startsWith("asin(") && expr.endsWith(")")) return Math.asin(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("acos(") && expr.endsWith(")")) return Math.acos(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("atan(") && expr.endsWith(")")) return Math.atan(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("sinh(") && expr.endsWith(")")) return Math.sinh(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("sh(") && expr.endsWith(")")) return Math.sinh(evalSub(expr.substring(3, expr.length() - 1), x));
        if (expr.startsWith("cosh(") && expr.endsWith(")")) return Math.cosh(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("tanh(") && expr.endsWith(")")) return Math.tanh(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("exp(") && expr.endsWith(")")) return Math.exp(evalSub(expr.substring(4, expr.length() - 1), x));
        if (expr.startsWith("log(") && expr.endsWith(")")) return Math.log(evalSub(expr.substring(4, expr.length() - 1), x));
        if (expr.startsWith("sqrt(") && expr.endsWith(")")) return Math.sqrt(evalSub(expr.substring(5, expr.length() - 1), x));
        if (expr.startsWith("abs(") && expr.endsWith(")")) return Math.abs(evalSub(expr.substring(4, expr.length() - 1), x));

        // Variable x
        if (expr.equals("x")) return x;
        if (expr.equals("-x")) return -x;

        // Número
        return Double.parseDouble(expr);
    }
}
