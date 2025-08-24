package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.*;
import com.neology.estacionamiento.repository.EstanciaRepository;
import com.neology.estacionamiento.repository.VehiculoNoResidenteRepository;
import com.neology.estacionamiento.repository.VehiculoOficialRepository;
import com.neology.estacionamiento.repository.VehiculoResidenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionamientoService {
    @Autowired
    private VehiculoOficialRepository vehiculoOficialRepository;
    @Autowired
    private VehiculoResidenteRepository vehiculoResidenteRepository;
    @Autowired
    private VehiculoNoResidenteRepository vehiculoNoResidenteRepository;
    @Autowired
    private EstanciaRepository estanciaRepository;

    public void registrarEntrada(String placa) {
        Optional<Estancia> estanciaActiva = estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa);
        if (estanciaActiva.isPresent()) {
            throw new IllegalStateException("El vehículo con placa " + placa + " ya está en el estacionamiento.");
        }
        
        Vehiculo vehiculo;
        
        if (vehiculoOficialRepository.existsById(placa)) {
            vehiculo = vehiculoOficialRepository.findById(placa).get();
        } else if (vehiculoResidenteRepository.existsById(placa)) {
            vehiculo = vehiculoResidenteRepository.findById(placa).get();
        } else {
            vehiculo = new VehiculoNoResidente();
            vehiculo.setPlaca(placa);
            vehiculoNoResidenteRepository.save((VehiculoNoResidente) vehiculo);
        }
        
        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);
        estanciaRepository.save(estancia);
    }
    
    public double registrarSalida(String placa) {
        Estancia estancia = estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa)
            .orElseThrow(() -> new IllegalArgumentException("El vehículo con placa " + placa + " no está en el estacionamiento."));
        
        estancia.setHoraSalida(Calendar.getInstance());
        long minutos = calcularMinutos(estancia.getHoraEntrada(), estancia.getHoraSalida());
        
        double costo = 0.0;
        Vehiculo vehiculo = estancia.getVehiculo();

        if (vehiculo instanceof VehiculoResidente) {
            VehiculoResidente residente = (VehiculoResidente) vehiculo;
            residente.setTiempoAcumuladoMinutos(residente.getTiempoAcumuladoMinutos() + minutos);
            vehiculoResidenteRepository.save(residente);
        } else if (vehiculo instanceof VehiculoNoResidente) {
            costo = minutos * 0.5;
        }

        estanciaRepository.save(estancia);
        return costo;
    }

    public void daDeAltaOficial(String placa) {
        VehiculoOficial vehiculo = new VehiculoOficial();
        vehiculo.setPlaca(placa);
        vehiculoOficialRepository.save(vehiculo);
    }

    public void daDeAltaResidente(String placa) {
        VehiculoResidente vehiculo = new VehiculoResidente();
        vehiculo.setPlaca(placa);
        vehiculoResidenteRepository.save(vehiculo);
    }
    
    public void comienzaMes() {
        estanciaRepository.deleteAllInBatch();
        List<VehiculoResidente> residentes = vehiculoResidenteRepository.findAll();
        for (VehiculoResidente residente : residentes) {
            residente.setTiempoAcumuladoMinutos(0);
            vehiculoResidenteRepository.save(residente);
        }
    }

    public void generarInformePagosResidentes(String nombreArchivo) {
        List<VehiculoResidente> residentes = vehiculoResidenteRepository.findAll();
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            writer.write("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");
            for (VehiculoResidente residente : residentes) {
                double aPagar = residente.getTiempoAcumuladoMinutos() * 0.05;
                writer.write(String.format("%s\t%d\t%.2f\n", residente.getPlaca(), residente.getTiempoAcumuladoMinutos(), aPagar));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el informe de pagos", e);
        }
    }

    /** Helper para calcular la diferencia entre fechas */
    private long calcularMinutos(Calendar inicio, Calendar fin) {
        long diff = fin.getTimeInMillis() - inicio.getTimeInMillis();
        return diff / (60 * 1000);
    }
}
