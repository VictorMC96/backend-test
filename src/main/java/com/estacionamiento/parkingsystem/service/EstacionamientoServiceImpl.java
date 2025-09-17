package com.estacionamiento.parkingsystem.service;

import com.estacionamiento.parkingsystem.model.*;
import com.estacionamiento.parkingsystem.repository.EstanciaRepository;
import com.estacionamiento.parkingsystem.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacionamientoServiceImpl implements  EstacionamientoService{

    private final VehiculoRepository vehiculoRepository;
    private final EstanciaRepository estanciaRepository;

    private static final double TARIFA_RESIDENTE = 0.05;
    private static final double TARIFA_NO_RESIDENTE = 0.5;

    @Override
    @Transactional
    public void registrarEntrada(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado: " + placa));

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);
        estancia.setHoraEntrada(LocalDateTime.now());

        estanciaRepository.save(estancia);
    }

    @Override
    @Transactional
    public double registrarSalida(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado: " + placa));

        Estancia estancia = estanciaRepository.findFirstByVehiculoAndHoraSalidaIsNullOrderByHoraEntradaDesc(vehiculo);
        if (estancia == null) {
            throw new RuntimeException("El vehículo no tiene una estancia abierta");
        }

        estancia.setHoraSalida(LocalDateTime.now());
        estanciaRepository.save(estancia);

        long minutos = estancia.getDuracionEnMinutos();
        double importe = 0.0;

        if (vehiculo instanceof VehiculoResidente residente) {
            residente.setTiempoAcumuladoMinutos(residente.getTiempoAcumuladoMinutos() + (int) minutos);
            vehiculoRepository.save(residente);
        } else if (vehiculo instanceof VehiculoNoResidente) {
            importe = minutos * TARIFA_NO_RESIDENTE;
        }

        return importe;
    }

    @Override
    public void darDeAltaVehiculoOficial(String placa) {
        VehiculoOficial oficial = new VehiculoOficial();
        oficial.setPlaca(placa);
        oficial.setTipo(TipoVehiculo.OFICIAL);
        vehiculoRepository.save(oficial);
    }

    @Override
    public void darDeAltaVehiculoResidente(String placa) {
        VehiculoResidente residente = new VehiculoResidente();
        residente.setPlaca(placa);
        residente.setTipo(TipoVehiculo.RESIDENTE);
        residente.setTiempoAcumuladoMinutos(0);
        vehiculoRepository.save(residente);
    }

    @Override
    @Transactional
    public void comenzarMes() {
        // Eliminar estancias de vehículos oficiales
/*        List<Vehiculo> oficiales = vehiculoRepository.findByTipo(TipoVehiculo.OFICIAL);
        for (Vehiculo oficial : oficiales) {
            List<Estancia> estancias = estanciaRepository.findByVehiculo(oficial);
            estanciaRepository.deleteAll(estancias);
        }

        // Resetear tiempo de residentes
        List<Vehiculo> residentes = vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE);
        for (Vehiculo v : residentes) {
            if (v instanceof VehiculoResidente residente) {
                residente.setTiempoAcumuladoMinutos(0);
                vehiculoRepository.save(residente);
            }
        }*/

    ///////////////////////////////////////////////////////////////////////////////

        // 1. Borrar estancias de vehículos oficiales
        List<VehiculoOficial> oficiales = vehiculoRepository.findAllOficiales();
        for (VehiculoOficial v : oficiales) {
            List<Estancia> estancias = estanciaRepository.findAllByVehiculo(v);
            estanciaRepository.deleteAll(estancias);
        }

        // 2. Resetear tiempo acumulado de residentes
        List<VehiculoResidente> residentes = vehiculoRepository.findAllResidentes();
        for (VehiculoResidente r : residentes) {
            r.setTiempoAcumuladoMinutos(0);
            vehiculoRepository.save(r);
        }
    }

    @Override
    public String generarReporteResidentes() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("Placa\tTiempo (min)\tCantidad a pagar\n");

        List<Vehiculo> residentes = vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE);
        for (Vehiculo v : residentes) {
            if (v instanceof VehiculoResidente residente) {
                double importe = residente.getTiempoAcumuladoMinutos() * TARIFA_RESIDENTE;
                reporte.append(residente.getPlaca())
                        .append("\t")
                        .append(residente.getTiempoAcumuladoMinutos())
                        .append("\t")
                        .append(String.format("%.2f", importe))
                        .append("\n");
            }
        }
        return reporte.toString();
    }


    public void generarInformePagos(String nombreArchivo) throws IOException {
        List<VehiculoResidente> residentes = vehiculoRepository.findAllResidentes();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

            for (VehiculoResidente r : residentes) {
                double pago = r.getTiempoAcumuladoMinutos() * 0.05;
                writer.write(String.format("%s\t%d\t%.2f\n", r.getPlaca(), r.getTiempoAcumuladoMinutos(), pago));
            }
        }
    }

    }


