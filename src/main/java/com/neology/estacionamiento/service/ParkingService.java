package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.*;
import com.neology.estacionamiento.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final VehiculoRepository vehiculoRepository;

    public void registrarEntrada(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa).orElse(null);

        if (vehiculo == null) {
            vehiculo = new NoResidente(); // Por defecto
            vehiculo.setPlaca(placa);
        }

        vehiculo.setHoraEntrada(Calendar.getInstance());
        vehiculo.setHoraSalida(null);
        vehiculoRepository.save(vehiculo);
    }

    public double registrarSalida(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlacaAndHoraSalidaIsNull(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Calendar salida = Calendar.getInstance();
        vehiculo.setHoraSalida(salida);

        int minutos = calcularMinutos(vehiculo.getHoraEntrada(), salida);

        if (vehiculo instanceof Residente r) {
            r.setMinutosAcumulados(r.getMinutosAcumulados() + minutos);
            vehiculoRepository.save(r);
            return 0.0;
        } else if (vehiculo instanceof NoResidente) {
            vehiculoRepository.save(vehiculo);
            return minutos * 0.5;
        }

        vehiculoRepository.save(vehiculo);
        return 0.0;
    }

    public void altaOficial(String placa) {
        Oficial oficial = new Oficial();
        oficial.setPlaca(placa);
        vehiculoRepository.save(oficial);
    }

    public void altaResidente(String placa) {
        Residente residente = new Residente();
        residente.setPlaca(placa);
        residente.setMinutosAcumulados(0);
        vehiculoRepository.save(residente);
    }

    public void comenzarMes() {
        List<Vehiculo> todos = vehiculoRepository.findAll();
        for (Vehiculo v : todos) {
            v.setHoraEntrada(null);
            v.setHoraSalida(null);

            if (v instanceof Residente r) {
                r.setMinutosAcumulados(0);
            }
        }
        vehiculoRepository.saveAll(todos);
    }

    public String generarReporteResidentes() {
        StringBuilder sb = new StringBuilder();
        sb.append("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

        List<Vehiculo> lista = vehiculoRepository.findAll();
        for (Vehiculo v : lista) {
            if (v instanceof Residente r) {
                int minutos = r.getMinutosAcumulados();
                double pago = minutos * 0.05;
                sb.append(r.getPlaca()).append("\t")
                  .append(minutos).append("\t")
                  .append(String.format("%.2f", pago)).append("\n");
            }
        }
        return sb.toString();
    }

    private int calcularMinutos(Calendar inicio, Calendar fin) {
        return (int) ((fin.getTimeInMillis() - inicio.getTimeInMillis()) / (1000 * 60));
    }
}
