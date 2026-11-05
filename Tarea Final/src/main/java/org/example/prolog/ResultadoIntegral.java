package org.example.prolog;

public class ResultadoIntegral {
    private final boolean exito;
    private final String primitiva;
    private final Double fa;
    private final Double fb;
    private final Double area;
    private final String mensajeError;

    public ResultadoIntegral(String primitiva, Double fa, Double fb, Double area) {
        this.exito = true;
        this.primitiva = primitiva;
        this.fa = fa;
        this.fb = fb;
        this.area = area;
        this.mensajeError = null;
    }

    public ResultadoIntegral(String primitiva) {
        this.exito = true;
        this.primitiva = primitiva;
        this.fa = null;
        this.fb = null;
        this.area = null;
        this.mensajeError = null;
    }

    public ResultadoIntegral(String mensajeError, boolean esError) {
        this.exito = false;
        this.primitiva = null;
        this.fa = null;
        this.fb = null;
        this.area = null;
        this.mensajeError = mensajeError;
    }

    public boolean isExito() {
        return exito;
    }

    public String getPrimitiva() {
        return primitiva != null ? primitiva : "Sin solución";
    }

    public Double getFa() {
        return fa;
    }

    public Double getFb() {
        return fb;
    }

    public Double getArea() {
        return area;
    }

    public boolean tieneEvaluacion() {
        return area != null && !Double.isNaN(area);
    }

    public String getMensajeError() {
        return mensajeError != null ? mensajeError : "";
    }
}
