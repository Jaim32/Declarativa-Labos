package org.example.ui.components;

import org.example.prolog.ResultadoIntegral;
import org.example.ui.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class PanelResultado extends JPanel {
    private final JTextArea txtPrimitiva = new JTextArea(2, 20);
    private final JLabel lblAreaValor = new JLabel("--");
    private final JLabel lblDetalleCalculo = new JLabel("Ingrese función y límites para calcular");
    private final JLabel lblEstado = new JLabel("Listo");

    public PanelResultado() {
        super(new BorderLayout(0, 10));
        setOpaque(false);

        // Contenedor principal estilo tarjeta
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));

        // 1. Sección Primitiva F(x)
        JPanel seccionPrimitiva = new JPanel(new BorderLayout(8, 4));
        seccionPrimitiva.setOpaque(false);

        JLabel lblTitPrimitiva = new JLabel("PRIMITIVA SIMBÓLICA F(x) + C");
        lblTitPrimitiva.setFont(Theme.sans(Font.BOLD, 11));
        lblTitPrimitiva.setForeground(Theme.MUTED);

        txtPrimitiva.setEditable(false);
        txtPrimitiva.setLineWrap(true);
        txtPrimitiva.setWrapStyleWord(true);
        txtPrimitiva.setFont(Theme.mono(Font.BOLD, 16));
        txtPrimitiva.setForeground(Theme.TEXT);
        txtPrimitiva.setBackground(Theme.CARD);
        txtPrimitiva.setBorder(null);

        Buttons btnCopiar = new Buttons("Copiar", Theme.BORDER, Theme.TEXT, 12, 4, 11);
        btnCopiar.addActionListener(e -> {
            String t = txtPrimitiva.getText();
            if (!t.isEmpty() && !t.contains("Sin solución")) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(t), null);
                setEstado("Copiado al portapapeles", Theme.BLUE);
            }
        });

        seccionPrimitiva.add(lblTitPrimitiva, BorderLayout.NORTH);
        seccionPrimitiva.add(txtPrimitiva, BorderLayout.CENTER);
        seccionPrimitiva.add(btnCopiar, BorderLayout.EAST);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setBackground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));

        // 2. Sección Área Bajo la Curva
        JPanel seccionArea = new JPanel(new BorderLayout(6, 4));
        seccionArea.setOpaque(false);

        JLabel lblTitArea = new JLabel("VALOR DEL ÁREA BAJO LA CURVA [a, b]");
        lblTitArea.setFont(Theme.sans(Font.BOLD, 11));
        lblTitArea.setForeground(Theme.MUTED);

        lblAreaValor.setFont(Theme.mono(Font.BOLD, 22));
        lblAreaValor.setForeground(Theme.GREEN);

        lblDetalleCalculo.setFont(Theme.sans(Font.PLAIN, 12));
        lblDetalleCalculo.setForeground(Theme.MUTED);

        seccionArea.add(lblTitArea, BorderLayout.NORTH);
        seccionArea.add(lblAreaValor, BorderLayout.CENTER);
        seccionArea.add(lblDetalleCalculo, BorderLayout.SOUTH);

        // Ensamblar tarjeta
        card.add(seccionPrimitiva);
        card.add(Box.createVerticalStrut(10));
        card.add(sep);
        card.add(Box.createVerticalStrut(10));
        card.add(seccionArea);

        // Pie: Estado
        lblEstado.setFont(Theme.sans(Font.BOLD, 12));
        lblEstado.setForeground(Theme.MUTED);

        add(card, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    public void mostrarResultado(ResultadoIntegral r, String funcion, Double a, Double b) {
        if (!r.isExito()) {
            txtPrimitiva.setText("Sin solución para esa expresión");
            lblAreaValor.setText("--");
            lblDetalleCalculo.setText(r.getMensajeError());
            setEstado("Error: " + r.getMensajeError(), Theme.RED);
            return;
        }

        txtPrimitiva.setText(r.getPrimitiva() + " + C");

        if (r.tieneEvaluacion()) {
            double area = r.getArea();
            lblAreaValor.setText(String.format("%.6f u²", area));
            lblAreaValor.setForeground(Theme.GREEN);

            String detalle = String.format("Regla de Barrow: F(%.4f) - F(%.4f) = %.6f - (%.6f) = %.6f",
                    b, a, r.getFb(), r.getFa(), area);
            lblDetalleCalculo.setText(detalle);
            setEstado("Cálculo completado con éxito mediante SWI-Prolog", Theme.GREEN);
        } else {
            lblAreaValor.setText("(Solo integral indefinida)");
            lblAreaValor.setForeground(Theme.MUTED);
            lblDetalleCalculo.setText("Para calcular el área proporcione los límites a y b");
            setEstado("Antiderivada encontrada", Theme.BLUE);
        }
    }

    public void setEstado(String texto, Color c) {
        lblEstado.setText(texto);
        lblEstado.setForeground(c);
    }

    public void limpiar() {
        txtPrimitiva.setText("");
        lblAreaValor.setText("--");
        lblDetalleCalculo.setText("Ingrese función y límites para calcular");
        setEstado("Listo", Theme.MUTED);
    }
}
