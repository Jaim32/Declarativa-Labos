package org.example;

import org.example.prolog.PrologEngine;
import org.example.ui.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look & Feel nativo del sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Inicializar motor Prolog y lanzar interfaz
                PrologEngine motor = new PrologEngine();
                VentanaPrincipal ventana = new VentanaPrincipal(motor);
                ventana.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al inicializar el motor de SWI-Prolog:\n" + ex.getMessage(),
                        "Error de Inicialización",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
