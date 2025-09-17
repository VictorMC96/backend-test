package com.estacionamiento.parkingsystem.service;

public interface EstacionamientoService {
    void registrarEntrada(String placa);
    double registrarSalida(String placa);
    void darDeAltaVehiculoOficial(String placa);
    void darDeAltaVehiculoResidente(String placa);
    void comenzarMes();
    String generarReporteResidentes();

}
