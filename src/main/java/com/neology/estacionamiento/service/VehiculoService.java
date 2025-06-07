package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.Estancia;
import com.neology.estacionamiento.model.TipoVehiculo;
import com.neology.estacionamiento.model.Vehiculo;

import java.util.List;
import java.util.Map;

public interface VehiculoService {
    Vehiculo registrarVehiculo(Vehiculo vehiculo, TipoVehiculo tipo);
    Estancia registrarEntrada(String placa);
    String registrarSalida(String placa);
    List<Map<String, Object>> generarReporteResidentes();
    void reiniciarMes();
    String generarReporte(List<Map<String, Object>> reporte, String nombreArchivo);
}
