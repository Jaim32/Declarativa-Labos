package org.example.screen;

import org.example.Connection;
import org.example.screen.components.Buttons;
import org.example.screen.components.PanelResult;
import org.example.screen.components.TextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/** Ventana principal de la Calculadora de Operaciones Básicas en Prolog. */
public class Window extends JFrame {

    private static final String[] EJEMPLOS = {
            "15 + 25", "100 - 35", "12 * 8", "144 / 12", "50 + 10 * 3"
    };

    private final TextField txtEntrada = new TextField("Ej: 15 + 25  o  20, 5");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel(" ");

    public Window(Connection motor) {
        super("Calculadora de Operaciones Básicas en Prolog");

        // Enter en el campo = calcular
        txtEntrada.addActionListener(e -> ejecutar(motor, "calcular"));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, etiqueta("Expresión u Operación"), 6);
        agregar(contenido, txtEntrada, 8);
        agregar(contenido, crearEjemplos(), 18);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 10);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(720, 540));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    //  Construcción de secciones
    // ------------------------------------------------------------------

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora Básica");
        titulo.setFont(Theme.sans(Font.BOLD, 24));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Las 4 operaciones básicas resueltas con reglas en Prolog (+, -, *, /)");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitulo);
        return p;
    }

    private JPanel crearEjemplos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Ejemplos:");
        lbl.setForeground(Theme.MUTED);
        lbl.setFont(Theme.sans(Font.PLAIN, 12));
        p.add(lbl);

        for (String ej : EJEMPLOS) {
            Buttons chip = new Buttons(ej, Theme.CARD, Theme.TEXT, 12, 6, 11);
            chip.addActionListener(e -> {
                txtEntrada.setText(ej);
                txtEntrada.requestFocusInWindow();
            });
            p.add(chip);
        }
        return p;
    }

    private JPanel crearBotones(Connection motor) {
        Buttons btnSuma = new Buttons("Suma (+)", Theme.BLUE, Theme.BG, 14, 10, 13);
        Buttons btnResta = new Buttons("Resta (-)", Theme.AMBER, Theme.BG, 14, 10, 13);
        Buttons btnMult = new Buttons("Multiplicación (*)", Theme.GREEN, Theme.BG, 14, 10, 13);
        Buttons btnDiv = new Buttons("División (/)", Theme.RED, Theme.BG, 14, 10, 13);
        Buttons btnCalcular = new Buttons("Calcular (=)", new Color(0x9D4EDD), Theme.TEXT, 16, 10, 13);

        btnSuma.addActionListener(e -> ejecutar(motor, "suma"));
        btnResta.addActionListener(e -> ejecutar(motor, "resta"));
        btnMult.addActionListener(e -> ejecutar(motor, "multiplicacion"));
        btnDiv.addActionListener(e -> ejecutar(motor, "division"));
        btnCalcular.addActionListener(e -> ejecutar(motor, "calcular"));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        p.add(btnSuma);
        p.add(btnResta);
        p.add(btnMult);
        p.add(btnDiv);
        p.add(btnCalcular);
        return p;
    }

    private JPanel crearPie() {
        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        Buttons btnCopiar = new Buttons("Copiar", Theme.CARD, Theme.TEXT, 16, 7, 12);
        btnCopiar.addActionListener(e -> copiarResultado());

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lblEstado, BorderLayout.WEST);
        p.add(btnCopiar, BorderLayout.EAST);
        return p;
    }

    // ------------------------------------------------------------------
    //  Lógica de ejecución
    // ------------------------------------------------------------------

    private void ejecutar(Connection motor, String op) {
        String entrada = txtEntrada.getText().trim();

        if (entrada.isEmpty()) {
            mostrar("", "Escribí una operación o números primero.", Theme.AMBER);
            return;
        }

        // Título descriptivo para el panel
        String nombreOp = switch (op) {
            case "suma" -> "Suma";
            case "resta" -> "Resta";
            case "multiplicacion" -> "Multiplicación";
            case "division" -> "División";
            default -> "Resultado del Cálculo";
        };
        panelResultado.setTitulo(nombreOp);

        try {
            String resultado = motor.consultar(op, entrada);
            if (resultado.startsWith("Sin solución") || resultado.startsWith("Error")) {
                mostrar("", resultado, Theme.AMBER);
            } else {
                mostrar(resultado, "Listo", Theme.GREEN);
            }
        } catch (Exception ex) {
            mostrar("", "Expresión inválida: " + ex.getMessage(), Theme.RED);
        }
    }

    private void mostrar(String resultado, String estado, Color colorEstado) {
        panelResultado.setTexto(resultado);
        lblEstado.setText(estado);
        lblEstado.setForeground(colorEstado);
    }

    private void copiarResultado() {
        String t = panelResultado.getTexto();
        if (t.isEmpty()) {
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(t), null);
        lblEstado.setText("Copiado al portapapeles");
        lblEstado.setForeground(Theme.BLUE);
    }

    // ------------------------------------------------------------------
    //  Utilidades
    // ------------------------------------------------------------------

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(Font.BOLD, 12));
        l.setForeground(Theme.MUTED);
        return l;
    }
}
