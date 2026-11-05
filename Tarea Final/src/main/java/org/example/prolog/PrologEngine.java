package org.example.prolog;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PrologEngine {
    private final String swiplBinary;
    private final File scriptFile;

    public PrologEngine() throws Exception {
        this.swiplBinary = detectarSwipl();
        this.scriptFile = extraerScriptProlog();
    }

    public ResultadoIntegral calcularDefinida(String funcion, double a, double b) {
        try {
            // Formatear consulta para SWI-Prolog
            // Query: resolver(x, <funcion>, <a>, <b>)
            String consulta = String.format("resolver(x, %s, %s, %s), halt.", funcion, a, b);
            List<String> lineas = ejecutarConsulta(consulta);
            return procesarSalida(lineas, true);
        } catch (Exception ex) {
            return new ResultadoIntegral("Error al consultar Prolog: " + ex.getMessage(), true);
        }
    }

    public ResultadoIntegral calcularIndefinida(String funcion) {
        try {
            String consulta = String.format("resolver_indefinida(x, %s), halt.", funcion);
            List<String> lineas = ejecutarConsulta(consulta);
            return procesarSalida(lineas, false);
        } catch (Exception ex) {
            return new ResultadoIntegral("Error al consultar Prolog: " + ex.getMessage(), true);
        }
    }

    private List<String> ejecutarConsulta(String goal) throws Exception {
        List<String> comando = new ArrayList<>();
        comando.add(swiplBinary);
        comando.add("-q");
        comando.add("-s");
        comando.add(scriptFile.getAbsolutePath());
        comando.add("-g");
        comando.add(goal);

        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea.trim());
            }
        }
        p.waitFor();
        return lineas;
    }

    private ResultadoIntegral procesarSalida(List<String> lineas, boolean requiereEval) {
        String status = "";
        String primitiva = null;
        Double fa = null;
        Double fb = null;
        Double area = null;
        String error = null;

        for (String l : lineas) {
            if (l.startsWith("STATUS:")) {
                status = l.substring(7);
            } else if (l.startsWith("PRIMITIVA:")) {
                primitiva = l.substring(10);
            } else if (l.startsWith("FA:")) {
                try { fa = Double.parseDouble(l.substring(3).trim()); } catch (Exception ignored) {}
            } else if (l.startsWith("FB:")) {
                try { fb = Double.parseDouble(l.substring(3).trim()); } catch (Exception ignored) {}
            } else if (l.startsWith("AREA:")) {
                try { area = Double.parseDouble(l.substring(5).trim()); } catch (Exception ignored) {}
            } else if (l.startsWith("ERROR:")) {
                error = l.substring(6);
            }
        }

        if ("ERROR".equalsIgnoreCase(status) || primitiva == null) {
            return new ResultadoIntegral(error != null ? error : "No se encontró regla de integración", true);
        }

        if (requiereEval && area != null) {
            return new ResultadoIntegral(primitiva, fa, fb, area);
        }

        if (error != null) {
            return new ResultadoIntegral(primitiva + "  (Aviso: " + error + ")");
        }

        return new ResultadoIntegral(primitiva);
    }

    private static String detectarSwipl() {
        String[] candidatos = {
            "swipl",
            "/opt/homebrew/bin/swipl",
            "/usr/local/bin/swipl",
            "/usr/bin/swipl",
            "C:\\Program Files\\swipl\\bin\\swipl.exe"
        };
        for (String cand : candidatos) {
            try {
                Process p = new ProcessBuilder(cand, "--version").start();
                if (p.waitFor() == 0) {
                    return cand;
                }
            } catch (Exception ignored) {}
        }
        return "swipl";
    }

    private static File extraerScriptProlog() throws IOException {
        File temp = new File(System.getProperty("java.io.tmpdir"), "integrales_swi.pl");
        try (InputStream in = PrologEngine.class.getResourceAsStream("/integrales.pl")) {
            if (in != null) {
                Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return temp;
            }
        }
        File local = new File("src/main/resources/integrales.pl");
        if (local.exists()) {
            return local.getAbsoluteFile();
        }
        throw new FileNotFoundException("No se encontró el recurso integrales.pl");
    }
}
