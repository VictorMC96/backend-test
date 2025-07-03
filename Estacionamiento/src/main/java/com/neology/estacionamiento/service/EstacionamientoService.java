package com.neology.estacionamiento.service;

import com.neology.estacionamiento.domain.*;
import com.neology.estacionamiento.repository.EstanciaRepository;
import com.neology.estacionamiento.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class EstacionamientoService {

    private final VehiculoRepository vehiculoRepo;
    private final EstanciaRepository estanciaRepo;

    public EstacionamientoService(VehiculoRepository vehiculoRepo, EstanciaRepository estanciaRepo) {
        this.vehiculoRepo = vehiculoRepo;
        this.estanciaRepo = estanciaRepo;
    }

    public void registrarEntrada(String placa) {
        Vehiculo v = vehiculoRepo.findById(placa).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        Estancia est = new Estancia(Calendar.getInstance());
        est.setVehiculo(v);
        estanciaRepo.save(est);
    }

    public double registrarSalida(String placa) {
        Vehiculo v = vehiculoRepo.findById(placa).orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        List<Estancia> estancias = v.getEstancias();
        Estancia abierta = estancias.stream()
                .filter(e -> e.getSalida() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay estancia abierta para este vehículo"));

        abierta.setSalida(Calendar.getInstance());

        int minutos = difEnMinutos(abierta.getEntrada(), abierta.getSalida());

        double cobro = 0;
        if (v.getTipo() == TipoVehiculo.RESIDENTE) {
            v.sumarTiempo(minutos);
            vehiculoRepo.save(v);
        } else if (v.getTipo() == TipoVehiculo.NO_RESIDENTE) {
            cobro = minutos * 0.5;
        }

        estanciaRepo.save(abierta);
        return cobro;
    }

    public void altaVehiculo(String placa, TipoVehiculo tipo) {
        Vehiculo v = new Vehiculo(placa, tipo);
        vehiculoRepo.save(v);
    }

    public void comienzaMes() {
        List<Vehiculo> vehiculos = vehiculoRepo.findAll();
        for (Vehiculo v : vehiculos) {
            if (v.getTipo() == TipoVehiculo.OFICIAL) {
                v.getEstancias().clear();
            } else if (v.getTipo() == TipoVehiculo.RESIDENTE) {
                v.resetAcumulado();
            }
            vehiculoRepo.save(v);
        }
        estanciaRepo.deleteAll();
    }

    private int difEnMinutos(Calendar inicio, Calendar fin) {
        long ms = fin.getTimeInMillis() - inicio.getTimeInMillis();
        return (int) (ms / 60000);
    }
}
