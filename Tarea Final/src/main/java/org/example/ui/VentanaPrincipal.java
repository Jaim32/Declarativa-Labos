package org.example.ui;

import org.example.prolog.PrologEngine;
import org.example.prolog.ResultadoIntegral;
import org.example.ui.components.Buttons;
import org.example.ui.components.PanelGrafico;
import org.example.ui.components.PanelResultado;
import org.example.ui.components.TextField;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final PrologEngine motor;

    private final TextField txtFuncion = new TextField("Ej: 3*x^2 + 2*x  o  sin(x)", 24);
    private final TextField txtLimiteA = new TextField("0.0", 8);
    private final TextField txtLimiteB = new TextField("2.0", 8);

    private final PanelResultado panelResultado = new PanelResultado();
    private final PanelGrafico panelGrafico = new PanelGrafico();

    // Ejemplos representativos de las 22 integrales exigidas
    private static final String[][] EJEMPLOS_GRUPOS = {
        {"3*x^2 + 2*x", "x^3", "3*x", "5", "1/x"},
        {"sin(x)", "cos(x)", "tan(x)"},
        {"asin(x)", "acos(x)", "atan(x)"},
        {"exp(x)", "2^x"},
        {"sinh(x)", "cosh(x)", "tanh(x)", "asinh(x)"}
    };

    public VentanaPrincipal(PrologEngine motor) {
        super("Proyecto Final - Sistema Experto de Integrales (SWI-Prolog)");
        this.motor = motor;

        // Enter en el campo de texto ejecuta el cálculo completo
        txtFuncion.addActionListener(e -> calcularAreaCompleta());
        txtLimiteA.addActionListener(e -> calcularAreaCompleta());
        txtLimiteB.addActionListener(e -> calcularAreaCompleta());

        // Contenedor principal con división horizontal (Izquierda: controles, Derecha: gráfico)
        JPanel panelIzquierdo = crearPanelIzquierdo();
        JPanel panelDerecho = crearPanelDerecho();

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);
        cuerpo.add(panelIzquierdo, BorderLayout.CENTER);
        cuerpo.add(panelDerecho, BorderLayout.EAST);

        JPanel raiz = new JPanel(new BorderLayout(0, 16));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(20, 24, 20, 24));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(cuerpo, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(1020, 680));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel titulo = new JLabel("Sistema Experto de Integrales");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Solución simbólica de integrales y cálculo del área bajo la curva mediante SWI-Prolog");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitulo);
        return p;
    }

    private JPanel crearPanelIzquierdo() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // 1. Campo Función f(x)
        p.add(etiqueta("Función a Integrar f(x)"));
        p.add(Box.createVerticalStrut(6));
        txtFuncion.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(txtFuncion);
        p.add(Box.createVerticalStrut(12));

        // 2. Límites a y b
        p.add(etiqueta("Límites de Integración [a, b]"));
        p.add(Box.createVerticalStrut(6));
        JPanel panelLimites = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelLimites.setOpaque(false);
        panelLimites.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblA = new JLabel("Límite a:");
        lblA.setFont(Theme.sans(Font.BOLD, 12));
        lblA.setForeground(Theme.MUTED);

        JLabel lblB = new JLabel("Límite b:");
        lblB.setFont(Theme.sans(Font.BOLD, 12));
        lblB.setForeground(Theme.MUTED);

        panelLimites.add(lblA);
        panelLimites.add(txtLimiteA);
        panelLimites.add(lblB);
        panelLimites.add(txtLimiteB);
        p.add(panelLimites);
        p.add(Box.createVerticalStrut(14));

        // 3. Chips de Ejemplos
        p.add(crearChipsEjemplos());
        p.add(Box.createVerticalStrut(14));

        // 4. Botones de Acción
        p.add(crearBotonesAccion());
        p.add(Box.createVerticalStrut(16));

        // 5. Panel de Resultados
        panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(panelResultado);

        return p;
    }

    private JPanel crearPanelDerecho() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(380, 520));

        JLabel lblTituloGrafico = new JLabel("Visualización Gráfica");
        lblTituloGrafico.setFont(Theme.sans(Font.BOLD, 13));
        lblTituloGrafico.setForeground(Theme.MUTED);

        p.add(lblTituloGrafico, BorderLayout.NORTH);
        p.add(panelGrafico, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearChipsEjemplos() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Ejemplos rápidos (las 22 integrales soportadas):");
        lbl.setFont(Theme.sans(Font.BOLD, 11));
        lbl.setForeground(Theme.MUTED);
        p.add(lbl);
        p.add(Box.createVerticalStrut(6));

        for (String[] grupo : EJEMPLOS_GRUPOS) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
            fila.setOpaque(false);
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String ej : grupo) {
                Buttons chip = new Buttons(ej, Theme.CARD, Theme.TEXT, 10, 4, 11);
                chip.addActionListener(e -> {
                    txtFuncion.setText(ej);
                    ajustarLimitesPorDefecto(ej);
                    txtFuncion.requestFocusInWindow();
                });
                fila.add(chip);
            }
            p.add(fila);
        }
        return p;
    }

    private JPanel crearBotonesAccion() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        Buttons btnCalcular = new Buttons("Calcular Integral y Área", Theme.GREEN, Theme.BG, 18, 10, 13);
        Buttons btnIndefinida = new Buttons("Solo Primitiva F(x)", Theme.BLUE, Theme.BG, 16, 10, 13);
        Buttons btnLimpiar = new Buttons("Limpiar", Theme.BORDER, Theme.TEXT, 14, 10, 13);

        btnCalcular.addActionListener(e -> calcularAreaCompleta());
        btnIndefinida.addActionListener(e -> calcularSoloIndefinida());
        btnLimpiar.addActionListener(e -> limpiarTodo());

        p.add(btnCalcular);
        p.add(btnIndefinida);
        p.add(btnLimpiar);
        return p;
    }

    private void calcularAreaCompleta() {
        String func = txtFuncion.getText().trim();
        if (func.isEmpty()) {
            panelResultado.setEstado("Escriba una función primero", Theme.AMBER);
            return;
        }

        double a, b;
        try {
            a = Double.parseDouble(txtLimiteA.getText().trim());
            b = Double.parseDouble(txtLimiteB.getText().trim());
        } catch (NumberFormatException ex) {
            panelResultado.setEstado("Límites a y b deben ser valores numéricos válidos", Theme.RED);
            return;
        }

        panelResultado.setEstado("Consultando a SWI-Prolog...", Theme.BLUE);

        // Ejecutar en hilo de fondo para mantener la UI reactiva
        new SwingWorker<ResultadoIntegral, Void>() {
            @Override
            protected ResultadoIntegral doInBackground() {
                return motor.calcularDefinida(func, a, b);
            }

            @Override
            protected void done() {
                try {
                    ResultadoIntegral r = get();
                    panelResultado.mostrarResultado(r, func, a, b);
                    if (r.isExito()) {
                        panelGrafico.actualizarGrafico(func, a, b);
                    }
                } catch (Exception ex) {
                    panelResultado.setEstado("Error: " + ex.getMessage(), Theme.RED);
                }
            }
        }.execute();
    }

    private void calcularSoloIndefinida() {
        String func = txtFuncion.getText().trim();
        if (func.isEmpty()) {
            panelResultado.setEstado("Escriba una función primero", Theme.AMBER);
            return;
        }

        panelResultado.setEstado("Consultando primitiva a SWI-Prolog...", Theme.BLUE);

        new SwingWorker<ResultadoIntegral, Void>() {
            @Override
            protected ResultadoIntegral doInBackground() {
                return motor.calcularIndefinida(func);
            }

            @Override
            protected void done() {
                try {
                    ResultadoIntegral r = get();
                    panelResultado.mostrarResultado(r, func, null, null);
                    if (r.isExito()) {
                        panelGrafico.actualizarGrafico(func, null, null);
                    }
                } catch (Exception ex) {
                    panelResultado.setEstado("Error: " + ex.getMessage(), Theme.RED);
                }
            }
        }.execute();
    }

    private void limpiarTodo() {
        txtFuncion.setText("");
        txtLimiteA.setText("0.0");
        txtLimiteB.setText("2.0");
        panelResultado.limpiar();
        panelGrafico.limpiar();
    }

    private void ajustarLimitesPorDefecto(String ej) {
        if (ej.contains("asin") || ej.contains("acos")) {
            txtLimiteA.setText("0.0");
            txtLimiteB.setText("0.8");
        } else if (ej.contains("atanh")) {
            txtLimiteA.setText("0.0");
            txtLimiteB.setText("0.5");
        } else if (ej.contains("acosh")) {
            txtLimiteA.setText("1.5");
            txtLimiteB.setText("3.0");
        } else if (ej.equals("1/x")) {
            txtLimiteA.setText("1.0");
            txtLimiteB.setText("2.718");
        } else if (ej.contains("sin") || ej.contains("cos")) {
            txtLimiteA.setText("0.0");
            txtLimiteB.setText("3.1416");
        } else {
            txtLimiteA.setText("0.0");
            txtLimiteB.setText("2.0");
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(Font.BOLD, 12));
        l.setForeground(Theme.MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
