package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.Estancia;
import com.neology.estacionamiento.model.TipoVehiculo;
import com.neology.estacionamiento.model.Vehiculo;
import com.neology.estacionamiento.repository.EstanciaRepository;
import com.neology.estacionamiento.repository.VehiculoRepository;
import com.neology.estacionamiento.util.Constantes;
import com.neology.estacionamiento.util.FechaUtils;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final EstanciaRepository estanciaRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository, EstanciaRepository estanciaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.estanciaRepository = estanciaRepository;
    }

    @Override
    public Vehiculo registrarVehiculo(Vehiculo vehiculo, TipoVehiculo tipo) {
    	
        if (vehiculoRepository.existsById(vehiculo.getPlaca())) {
            throw new IllegalStateException(Constantes.VEHICULO_YA_REGISTRADO);
        }

        vehiculo.setTipo(tipo);
        return vehiculoRepository.save(vehiculo);
    }
    
    @Override
    public Estancia registrarEntrada(String placa) {
     
        if (!vehiculoRepository.existsById(placa)) {
            throw new IllegalStateException(Constantes.VEHICULO_NO_REGISTRADO);
        }


        Optional<Estancia> activa = estanciaRepository.findByPlacaAndHoraSalidaIsNull(placa);
        if (activa.isPresent()) {
            throw new IllegalStateException(Constantes.ENTRADA_ACTIVA);
        }

        Estancia estancia = new Estancia(placa, LocalDateTime.now());
        return estanciaRepository.save(estancia);
    }

    @Override
    
    public String registrarSalida(String placa) {
        Estancia estancia = estanciaRepository.findByPlacaAndHoraSalidaIsNull(placa)
            .orElseThrow(() -> new IllegalStateException(Constantes.NO_HAY_ENTRADA));
        estancia.setHoraSalida(LocalDateTime.now());
        estanciaRepository.save(estancia);

        Vehiculo vehiculo = vehiculoRepository.findById(placa)
            .orElseThrow(() -> new IllegalStateException(Constantes.VEHICULO_NO_REGISTRADO));

        TipoVehiculo tipo = vehiculo.getTipo();
        int minutos = FechaUtils.difEnMinutos(estancia.getHoraEntrada(), estancia.getHoraSalida());

        switch (tipo) {
            case OFICIAL:
                return Constantes.SALIDA_REGISTRADA_VEHICULOS_OFICIALES;

            case RESIDENTE:
                int nuevosMinutos = vehiculo.getMinutosAcumulados() + (int) minutos;
                vehiculo.setMinutosAcumulados(nuevosMinutos);
                vehiculoRepository.save(vehiculo);
                return String.format(Constantes.SALIDA_RESIDENTE_MINUTOS, minutos);

            case NO_RESIDENTE:
            default:
                double costo = minutos * 0.5;
                return String.format(Constantes.SALIDA_NO_RESIDENTE_TOTAL, costo);
        }
    }
    

    @Override
    public List<Map<String, Object>> generarReporteResidentes() {
        return vehiculoRepository.findAll().stream()
                .filter(v -> v.getTipo() == TipoVehiculo.RESIDENTE)
                .map(v -> {
                    Map<String, Object> reporte = new HashMap<>();
                    reporte.put("placa", v.getPlaca());
                    reporte.put("minutos", v.getMinutosAcumulados());
                    double total = v.getMinutosAcumulados() * 0.05;
                    reporte.put("total", String.format("%.2f", total));
                    return reporte;
                }).collect(Collectors.toList());
    }
    
    @Override
    public String generarReporte(List<Map<String, Object>> reporte, String nombreArchivo) {
        StringBuilder sb = new StringBuilder();
        sb.append("Placa,Minutos,Total\n");
        for (Map<String, Object> fila : reporte) {
            sb.append(fila.get("placa")).append(",");
            sb.append(fila.get("minutos")).append(",");
            sb.append(fila.get("total")).append("\n");
        }

        byte[] csvBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(csvBytes);
    }

    @Override
    public void reiniciarMes() {
        estanciaRepository.deleteAll();
        List<Vehiculo> residentes = vehiculoRepository.findAll().stream()
                .filter(v -> v.getTipo() == TipoVehiculo.RESIDENTE)
                .peek(v -> v.setMinutosAcumulados(0))
                .collect(Collectors.toList());
        vehiculoRepository.saveAll(residentes);
    }
} 