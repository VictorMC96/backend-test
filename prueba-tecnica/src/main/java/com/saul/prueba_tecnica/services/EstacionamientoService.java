package com.saul.prueba_tecnica.services;

import com.saul.prueba_tecnica.entities.*;
import com.saul.prueba_tecnica.repositories.*;
import com.saul.prueba_tecnica.utils.FechaUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EstacionamientoService {

    private final VehiculoRepository vehiculoRepository;
    private final EstanciaRepository estanciaRepository;

    public EstacionamientoService(VehiculoRepository vehiculoRepository, EstanciaRepository estanciaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.estanciaRepository = estanciaRepository;
    }

    // Alta de vehículo oficial
    @Transactional
    public void registrarVehiculoOficial(String placa) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(TipoVehiculo.OFICIAL);
        vehiculoRepository.save(vehiculo);
    }

    // Alta de vehículo residente
    @Transactional
    public void registrarVehiculoResidente(String placa) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(TipoVehiculo.RESIDENTE);
        vehiculo.setMinutosAcumulados(0);
        vehiculoRepository.save(vehiculo);
    }

    // Registrar entrada
    @Transactional
    public void registrarEntrada(String placa) {
        Optional<Vehiculo> optionalVehiculo = vehiculoRepository.findById(placa);
        Vehiculo vehiculo = null;
        if (optionalVehiculo.isEmpty()) {
            vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setTipo(TipoVehiculo.NO_RESIDENTE);
            vehiculo.setMinutosAcumulados(0);
            vehiculoRepository.save(vehiculo);

        }else{
            vehiculo = optionalVehiculo.orElseThrow(
                () -> new RuntimeException("Vehículo no registrado"));
        }

        Estancia estancia = new Estancia();
        estancia.setEntrada(Calendar.getInstance().getTime());
        estancia.setVehiculo(vehiculo);

        estanciaRepository.save(estancia);
    }

    // Registrar salida
    @Transactional
    public double registrarSalida(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
                .orElseThrow(() -> new RuntimeException("Vehículo no registrado"));

        Estancia ultimaEstancia = estanciaRepository.findTopByVehiculoOrderByEntradaDesc(vehiculo);
        if (ultimaEstancia == null || ultimaEstancia.getSalida() != null) {
            throw new RuntimeException("No hay una estancia abierta para este vehículo");
        }

        Date salida = Calendar.getInstance().getTime();
        ultimaEstancia.setSalida(salida);

        Calendar entradaCal = Calendar.getInstance();
        entradaCal.setTime(ultimaEstancia.getEntrada());

        Calendar salidaCal = Calendar.getInstance();
        salidaCal.setTime(salida);

        int minutos = FechaUtils.difEnMinutos(entradaCal, salidaCal);

        switch (vehiculo.getTipo()) {
            case OFICIAL -> {
            }
            case RESIDENTE -> {
                vehiculo.setMinutosAcumulados(vehiculo.getMinutosAcumulados() + minutos);
            }
            case NO_RESIDENTE -> {
                double monto = minutos * 0.5;
                estanciaRepository.save(ultimaEstancia);
                return monto;
            }
        }

        estanciaRepository.save(ultimaEstancia);
        vehiculoRepository.save(vehiculo);
        return 0.0;
    }



    // Comenzar mes
    @Transactional
    public void comenzarMes() {
        List<Vehiculo> todos = vehiculoRepository.findAll();
        for (Vehiculo v : todos) {
            if (v.getTipo() == TipoVehiculo.OFICIAL) {
                List<Estancia> estancias = estanciaRepository.findByVehiculo(v);
                estanciaRepository.deleteAll(estancias);
            } else if (v.getTipo() == TipoVehiculo.RESIDENTE) {
                v.setMinutosAcumulados(0);
                vehiculoRepository.save(v);
            }
        }
    }

    // Generar reporte de residentes
    @Transactional
    public void generarReportePagos() {
        var residentes = vehiculoRepository.findAll().stream()
                .filter(v -> v.getTipo() == TipoVehiculo.RESIDENTE)
                .toList();

        var symbols = DecimalFormatSymbols.getInstance(Locale.US);
        var df = new DecimalFormat("0.00", symbols);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fecha = sdf.format(calendar.getTime());

        Path path = Paths.get("reporte_residentes_" + fecha + ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write(String.format("%-12s %-26s %-15s",
                    "Núm. placa", "Tiempo estacionado (min.)", "Cantidad a pagar"));
            writer.newLine();

            for (Vehiculo v : residentes) {
                double monto = v.getMinutosAcumulados() * 0.05;

                writer.write(String.format("%-12s %-26d %-15s",
                        v.getPlaca(), v.getMinutosAcumulados(), df.format(monto)));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al exportar el reporte: " + e.getMessage(), e);
        }

    }
}