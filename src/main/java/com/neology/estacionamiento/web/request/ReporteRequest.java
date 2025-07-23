package com.neology.estacionamiento.web.request;

/**
 * DTO para la solicitud de generación de reporte con nombre personalizado
 */
public class ReporteRequest {

    private String nombreArchivo;

    public ReporteRequest() {}

    public ReporteRequest(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public String toString() {
        return "ReporteRequest{nombreArchivo='" + nombreArchivo + "'}";
    }
} 