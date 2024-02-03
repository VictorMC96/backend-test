package com.examen.oodoo.service;

import com.examen.oodoo.entity.Estancia;
import com.examen.oodoo.entity.Vehiculo;
import com.examen.oodoo.enums.TipoVehiculo;
import com.examen.oodoo.repository.EstanciaRepository;
import com.examen.oodoo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final EstanciaRepository estanciaRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository, EstanciaRepository estanciaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.estanciaRepository = estanciaRepository;
    }

    public String registrarEntrada(String placa) {
        Vehiculo vehiculoExistente = vehiculoRepository.findByPlaca(placa);
        if (vehiculoExistente != null) {
            Estancia estanciaExistente = vehiculoExistente.getEstanciaActiva();
            if (estanciaExistente != null) {
                return "Ya existe una estancia activa para el vehículo con placa " + placa;
            } else {
                Estancia nuevaEstancia = new Estancia();
                nuevaEstancia.setHoraEntrada(Calendar.getInstance());
                nuevaEstancia.setVehiculo(vehiculoExistente);

                vehiculoExistente.getEstancias().add(nuevaEstancia);
                vehiculoRepository.save(vehiculoExistente);
                return "Registro de entrada exitoso para el vehículo con placa " + placa;
            }
        } else {
            return "El vehículo con placa " + placa + " no está registrado. No se puede registrar la entrada.";
        }
    }

    public void registrarSalida(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo != null) {

            Estancia ultimaEstancia = vehiculo.getEstancias().get(vehiculo.getEstancias().size() - 1);
            ultimaEstancia.setHoraSalida(Calendar.getInstance());
            estanciaRepository.save(ultimaEstancia);
        }
    }
    public String darDeAltaVehiculoOficial(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo == null) {
            Vehiculo vehiculoNuevo = new Vehiculo(placa, TipoVehiculo.OFICIAL);
            vehiculoRepository.save(vehiculoNuevo);
            return "Registro exitoso para el vehículo con placa " + placa;
        }
        return "El vehículo con placa " + placa + " ya está registrado. No se puede registrar de nuevo.";
    }

    public String darDeAltaVehiculoResidente(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
        if (vehiculo == null) {
            Vehiculo vehiculoNuevo = new Vehiculo(placa, TipoVehiculo.OFICIAL);
            vehiculoRepository.save(vehiculoNuevo);
            return "Registro exitoso para el vehículo con placa " + placa;
        }
        return "El vehículo con placa " + placa + " ya está registrado. No se puede registrar de nuevo.";
    }
}
