package org.example.ui.components;

import org.example.prolog.ResultadoIntegral;
import org.example.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class PanelResultado extends SurfacePanel {
    private final JTextArea txtPrimitiva = new JTextArea(2, 20);
    private final JLabel lblAreaValor = new JLabel("—");
    private final JTextArea lblDetalleCalculo = new JTextArea("Ingresa una función y pulsa calcular.");
    private final JLabel lblEstado = new JLabel("Listo para calcular");

    public PanelResultado() {
        super(20);
        setLayout(new BorderLayout(0, 12));
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JPanel titulos = new JPanel(new GridLayout(2, 1, 0, 3));
        titulos.setOpaque(false);
        titulos.add(label("04  /  RESULTADO", 11, Font.BOLD, Theme.GREEN));
        titulos.add(label("Solución", 18, Font.BOLD, Theme.TEXT));
        encabezado.add(titulos, BorderLayout.WEST);
        lblEstado.setFont(Theme.sans(Font.PLAIN, 11));
        lblEstado.setForeground(Theme.MUTED);
        encabezado.add(lblEstado, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        JPanel valores = new JPanel(new GridLayout(1, 2, 0, 0));
        valores.setOpaque(false);
        JPanel primitiva = new JPanel(new BorderLayout(0, 6));
        primitiva.setOpaque(false);
        primitiva.add(label("PRIMITIVA  ·  F(x) + C", 10, Font.BOLD, Theme.MUTED), BorderLayout.NORTH);
        txtPrimitiva.setEditable(false);
        txtPrimitiva.setFocusable(false);
        txtPrimitiva.setLineWrap(true);
        txtPrimitiva.setWrapStyleWord(true);
        txtPrimitiva.setFont(Theme.mono(Font.BOLD, 15));
        txtPrimitiva.setForeground(Theme.TEXT);
        txtPrimitiva.setBackground(Theme.CARD);
        txtPrimitiva.setText("—");
        primitiva.add(txtPrimitiva, BorderLayout.CENTER);
        Buttons copiar = new Buttons("Copiar resultado", Theme.CARD_HOVER, Theme.GREEN, 10, 5, 11);
        copiar.addActionListener(e -> {
            String valor = txtPrimitiva.getText();
            if (!valor.isBlank() && !valor.equals("—") && !valor.startsWith("Sin solución")) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(valor), null);
                setEstado("Copiado al portapapeles", Theme.GREEN);
            }
        });
        JPanel copiarFila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        copiarFila.setOpaque(false);
        copiarFila.add(copiar);
        primitiva.add(copiarFila, BorderLayout.SOUTH);

        JPanel area = new JPanel(new BorderLayout(0, 5));
        area.setOpaque(false);
        area.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        area.add(label("INTEGRAL DEFINIDA  ·  [a, b]", 10, Font.BOLD, Theme.MUTED), BorderLayout.NORTH);
        lblAreaValor.setFont(Theme.mono(Font.BOLD, 23));
        lblAreaValor.setForeground(Theme.GREEN);
        area.add(lblAreaValor, BorderLayout.CENTER);
        valores.add(primitiva);
        valores.add(area);
        add(valores, BorderLayout.CENTER);

        lblDetalleCalculo.setEditable(false);
        lblDetalleCalculo.setFocusable(false);
        lblDetalleCalculo.setLineWrap(true);
        lblDetalleCalculo.setWrapStyleWord(true);
        lblDetalleCalculo.setOpaque(false);
        lblDetalleCalculo.setFont(Theme.sans(Font.PLAIN, 11));
        lblDetalleCalculo.setForeground(Theme.MUTED);
        lblDetalleCalculo.setRows(2);
        add(lblDetalleCalculo, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(500, 205));
    }

    public void mostrarResultado(ResultadoIntegral r, String funcion, Double a, Double b) {
        if (!r.isExito()) {
            txtPrimitiva.setText("Sin solución para esa expresión");
            lblAreaValor.setText("—");
            lblDetalleCalculo.setText(r.getMensajeError());
            setEstado("No se pudo resolver", Theme.RED);
            return;
        }
        txtPrimitiva.setText(r.getPrimitiva() + " + C");
        if (r.tieneEvaluacion()) {
            double area = r.getArea();
            lblAreaValor.setText(String.format("%.6f", area));
            lblAreaValor.setForeground(Theme.GREEN);
            lblDetalleCalculo.setText(String.format("Regla de Barrow  ·  F(%.4f) − F(%.4f) = %.6f − (%.6f) = %.6f", b, a, r.getFb(), r.getFa(), area));
            setEstado("Cálculo completado", Theme.GREEN);
        } else {
            lblAreaValor.setText("—");
            lblDetalleCalculo.setText("Primitiva encontrada. Define un intervalo para calcular la integral definida.");
            setEstado("Primitiva encontrada", Theme.GREEN);
        }
    }

    public void setEstado(String texto, Color color) {
        lblEstado.setText(texto);
        lblEstado.setForeground(color);
    }

    public void limpiar() {
        txtPrimitiva.setText("—");
        lblAreaValor.setText("—");
        lblDetalleCalculo.setText("Ingresa una función y pulsa calcular.");
        setEstado("Listo para calcular", Theme.MUTED);
    }

    private static JLabel label(String texto, int tam, int estilo, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(estilo, tam));
        l.setForeground(color);
        return l;
    }
}
