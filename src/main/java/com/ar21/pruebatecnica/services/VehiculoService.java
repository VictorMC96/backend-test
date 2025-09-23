package com.ar21.pruebatecnica.services;

import com.ar21.pruebatecnica.entities.EntradaSalida;
import com.ar21.pruebatecnica.entities.Vehiculo;
import com.ar21.pruebatecnica.models.EntradaSalidaModel;
import com.ar21.pruebatecnica.models.VehiculoModel;

import java.util.Optional;

public interface VehiculoService {

    VehiculoModel registrarVehiculo(VehiculoModel vehiculo);

    EntradaSalidaModel registarSalida(String salida);

    Optional<VehiculoModel> buscarVehiculoPorPlaca(String placa);

    EntradaSalidaModel registrarEntrada(EntradaSalidaModel entrada);
}
