package org.example;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

import java.io.InputStream;

public class Connection {
    private final Prolog engine = new Prolog();

    public Connection() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/operaciones.pl")) {
            if (in == null) throw new IllegalStateException("No se encontró operaciones.pl");
            engine.setTheory(new Theory(in));
        }
    }

    public String consultar(String operacion, String expresion) throws Exception {
        expresion = expresion.trim();
        String query;

        if (expresion.contains(",")) {
            String[] partes = expresion.split(",");
            if (partes.length == 2) {
                String a = partes[0].trim();
                String b = partes[1].trim();
                query = operacion + "(" + a + ", " + b + ", R).";
            } else {
                query = "calcular(" + expresion + ", R).";
            }
        } else {
            query = "calcular(" + expresion + ", R).";
        }

        SolveInfo info = engine.solve(query);
        if (info.isSuccess()) {
            return info.getTerm("R").toString();
        } else {
            if (expresion.contains("/ 0") || expresion.contains("/0") || expresion.endsWith(", 0") || expresion.endsWith(",0")) {
                return "Error: División por cero no permitida";
            }
            return "Sin solución para esa expresión";
        }
    }
}
