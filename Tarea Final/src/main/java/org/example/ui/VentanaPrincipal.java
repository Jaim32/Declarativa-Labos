package org.example.ui;

import org.example.prolog.PrologEngine;
import org.example.prolog.ResultadoIntegral;
import org.example.ui.components.Buttons;
import org.example.ui.components.PanelGrafico;
import org.example.ui.components.PanelResultado;
import org.example.ui.components.SurfacePanel;
import org.example.ui.components.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final PrologEngine motor;
    private final TextField txtFuncion = new TextField("Ej. 3*x^2 + 2*x", 24);
    private final TextField txtLimiteA = new TextField("0", 7);
    private final TextField txtLimiteB = new TextField("2", 7);
    private final PanelResultado panelResultado = new PanelResultado();
    private final PanelGrafico panelGrafico = new PanelGrafico();
    private Buttons btnCalcular;
    private Buttons btnIndefinida;

    private static final String[][] EJEMPLOS_GRUPOS = {
        {"3*x^2 + 2*x", "x^3", "3*x", "5", "1/x"},
        {"sin(x)", "cos(x)", "tan(x)"},
        {"asin(x)", "acos(x)", "atan(x)"},
        {"exp(x)", "2^x"},
        {"sinh(x)", "cosh(x)", "tanh(x)", "asinh(x)"}
    };

    public VentanaPrincipal(PrologEngine motor) {
        super("Integral · Calculadora simbólica");
        this.motor = motor;
        txtFuncion.addActionListener(e -> calcularAreaCompleta());
        txtLimiteA.addActionListener(e -> calcularAreaCompleta());
        txtLimiteB.addActionListener(e -> calcularAreaCompleta());
        txtLimiteA.setText("0");
        txtLimiteB.setText("2");

        JPanel raiz = new JPanel(new BorderLayout(0, 26));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(28, 36, 30, 36));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(20, 0));
        cuerpo.setOpaque(false);
        JPanel izquierda = crearColumnaIzquierda();
        izquierda.setPreferredSize(new Dimension(430, 620));
        cuerpo.add(izquierda, BorderLayout.WEST);
        cuerpo.add(crearColumnaDerecha(), BorderLayout.CENTER);
        raiz.add(cuerpo, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(1030, 740));
        setSize(1220, 820);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(label("HERRAMIENTA DE CÁLCULO", 11, Font.BOLD, Theme.GREEN));
        textos.add(Box.createVerticalStrut(7));
        textos.add(label("Integrales, con claridad.", 30, Font.BOLD, Theme.TEXT));
        textos.add(Box.createVerticalStrut(5));
        textos.add(label("Escribe una función, define el intervalo y explora el resultado.", 14, Font.PLAIN, Theme.MUTED));
        p.add(textos, BorderLayout.WEST);

        return p;
    }

    private JPanel crearColumnaIzquierda() {
        JPanel columna = new JPanel(new BorderLayout(0, 18));
        columna.setOpaque(false);
        columna.add(crearEntrada(), BorderLayout.NORTH);
        columna.add(crearEjemplos(), BorderLayout.CENTER);
        return columna;
    }

    private JPanel crearEntrada() {
        SurfacePanel card = new SurfacePanel(22);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(label("01  /  CONFIGURACIÓN", 11, Font.BOLD, Theme.GREEN));
        card.add(Box.createVerticalStrut(7));
        card.add(label("Tu integral", 20, Font.BOLD, Theme.TEXT));
        card.add(Box.createVerticalStrut(20));
        card.add(label("Función f(x)", 12, Font.BOLD, Theme.TEXT));
        card.add(Box.createVerticalStrut(7));
        txtFuncion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        txtFuncion.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(txtFuncion);
        card.add(Box.createVerticalStrut(7));
        card.add(label("Usa * para multiplicar; por ejemplo, 3*x^2.", 11, Font.PLAIN, Theme.MUTED));
        card.add(Box.createVerticalStrut(20));
        card.add(label("Intervalo de integración", 12, Font.BOLD, Theme.TEXT));
        card.add(Box.createVerticalStrut(7));

        JPanel limites = new JPanel(new GridLayout(1, 2, 12, 0));
        limites.setOpaque(false);
        limites.setAlignmentX(Component.LEFT_ALIGNMENT);
        limites.add(campoLimite("Desde  ·  a", txtLimiteA));
        limites.add(campoLimite("Hasta  ·  b", txtLimiteB));
        card.add(limites);
        card.add(Box.createVerticalStrut(22));

        btnCalcular = new Buttons("Calcular integral  →", Theme.GREEN, Color.WHITE, 18, 12, 13);
        btnCalcular.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCalcular.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnCalcular.addActionListener(e -> calcularAreaCompleta());
        card.add(btnCalcular);
        card.add(Box.createVerticalStrut(10));

        JPanel secundarias = new JPanel(new BorderLayout(8, 0));
        secundarias.setOpaque(false);
        secundarias.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIndefinida = new Buttons("Solo primitiva", Theme.CARD_HOVER, Theme.GREEN, 13, 9, 12);
        btnIndefinida.addActionListener(e -> calcularSoloIndefinida());
        Buttons limpiar = new Buttons("Limpiar", Theme.CARD, Theme.MUTED, 13, 9, 12);
        limpiar.addActionListener(e -> limpiarTodo());
        secundarias.add(btnIndefinida, BorderLayout.CENTER);
        secundarias.add(limpiar, BorderLayout.EAST);
        card.add(secundarias);
        return card;
    }

    private JPanel campoLimite(String titulo, TextField campo) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.add(label(titulo, 11, Font.PLAIN, Theme.MUTED), BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearEjemplos() {
        SurfacePanel card = new SurfacePanel(22);
        card.setLayout(new BorderLayout(0, 12));
        JPanel cabecera = new JPanel(new GridLayout(2, 1, 0, 3));
        cabecera.setOpaque(false);
        cabecera.add(label("02  /  EXPLORAR", 11, Font.BOLD, Theme.GREEN));
        cabecera.add(label("Prueba una función", 18, Font.BOLD, Theme.TEXT));
        card.add(cabecera, BorderLayout.NORTH);

        JPanel grupos = new JPanel();
        grupos.setOpaque(false);
        grupos.setLayout(new BoxLayout(grupos, BoxLayout.Y_AXIS));
        for (String[] grupo : EJEMPLOS_GRUPOS) {
            JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3));
            fila.setOpaque(false);
            fila.setAlignmentX(Component.LEFT_ALIGNMENT);
            for (String ej : grupo) {
                Buttons chip = new Buttons(ej, Theme.CARD_HOVER, Theme.TEXT, 10, 6, 11);
                chip.addActionListener(e -> {
                    txtFuncion.setText(ej);
                    ajustarLimitesPorDefecto(ej);
                    txtFuncion.requestFocusInWindow();
                });
                fila.add(chip);
            }
            grupos.add(fila);
        }
        card.add(grupos, BorderLayout.CENTER);
        card.add(label("Selecciona un ejemplo para cargarlo en el campo.", 11, Font.PLAIN, Theme.MUTED), BorderLayout.SOUTH);
        return card;
    }

    private JPanel crearColumnaDerecha() {
        JPanel p = new JPanel(new BorderLayout(0, 18));
        p.setOpaque(false);
        SurfacePanel grafica = new SurfacePanel(20);
        grafica.setLayout(new BorderLayout(0, 14));
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(label("03  /  VISUALIZACIÓN", 11, Font.BOLD, Theme.GREEN), BorderLayout.NORTH);
        encabezado.add(label("La curva y su área", 18, Font.BOLD, Theme.TEXT), BorderLayout.SOUTH);
        grafica.add(encabezado, BorderLayout.NORTH);
        grafica.add(panelGrafico, BorderLayout.CENTER);
        grafica.setPreferredSize(new Dimension(500, 365));
        p.add(grafica, BorderLayout.CENTER);
        p.add(panelResultado, BorderLayout.SOUTH);
        return p;
    }

    private void calcularAreaCompleta() {
        String func = txtFuncion.getText().trim();
        if (func.isEmpty()) {
            panelResultado.setEstado("Escribe una función para continuar.", Theme.AMBER);
            txtFuncion.requestFocusInWindow();
            return;
        }
        double a, b;
        try {
            a = Double.parseDouble(txtLimiteA.getText().trim());
            b = Double.parseDouble(txtLimiteB.getText().trim());
            if (!Double.isFinite(a) || !Double.isFinite(b)) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            panelResultado.setEstado("Los límites deben ser números válidos.", Theme.RED);
            return;
        }
        ejecutarCalculo(func, a, b);
    }

    private void calcularSoloIndefinida() {
        String func = txtFuncion.getText().trim();
        if (func.isEmpty()) {
            panelResultado.setEstado("Escribe una función para continuar.", Theme.AMBER);
            txtFuncion.requestFocusInWindow();
            return;
        }
        ejecutarCalculo(func, null, null);
    }

    private void ejecutarCalculo(String func, Double a, Double b) {
        btnCalcular.setEnabled(false);
        btnIndefinida.setEnabled(false);
        panelResultado.setEstado("Calculando con SWI-Prolog…", Theme.BLUE);
        new SwingWorker<ResultadoIntegral, Void>() {
            @Override protected ResultadoIntegral doInBackground() {
                return a == null ? motor.calcularIndefinida(func) : motor.calcularDefinida(func, a, b);
            }
            @Override protected void done() {
                btnCalcular.setEnabled(true);
                btnIndefinida.setEnabled(true);
                try {
                    ResultadoIntegral r = get();
                    panelResultado.mostrarResultado(r, func, a, b);
                    if (r.isExito()) panelGrafico.actualizarGrafico(func, a, b);
                } catch (Exception ex) {
                    panelResultado.setEstado("Error: " + ex.getMessage(), Theme.RED);
                }
            }
        }.execute();
    }

    private void limpiarTodo() {
        txtFuncion.setText("");
        txtLimiteA.setText("0");
        txtLimiteB.setText("2");
        panelResultado.limpiar();
        panelGrafico.limpiar();
        txtFuncion.requestFocusInWindow();
    }

    private void ajustarLimitesPorDefecto(String ej) {
        if (ej.contains("asin") || ej.contains("acos")) {
            txtLimiteA.setText("0"); txtLimiteB.setText("0.8");
        } else if (ej.contains("atanh")) {
            txtLimiteA.setText("0"); txtLimiteB.setText("0.5");
        } else if (ej.contains("acosh")) {
            txtLimiteA.setText("1.5"); txtLimiteB.setText("3");
        } else if (ej.equals("1/x")) {
            txtLimiteA.setText("1"); txtLimiteB.setText("2.718");
        } else if (ej.contains("sin") || ej.contains("cos")) {
            txtLimiteA.setText("0"); txtLimiteB.setText("3.1416");
        } else {
            txtLimiteA.setText("0"); txtLimiteB.setText("2");
        }
    }

    private static JLabel label(String texto, int tam, int estilo, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(estilo, tam));
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}
