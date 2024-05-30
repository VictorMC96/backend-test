package com.ejercicio.estacionamiento.service;

import com.ejercicio.estacionamiento.exceptions.exceptions.PlacaNoEncontradaException;
import com.ejercicio.estacionamiento.model.Estancia;
import com.ejercicio.estacionamiento.model.Vehiculo;
import com.ejercicio.estacionamiento.repository.EstanciaRepository;
import com.ejercicio.estacionamiento.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class EstacionamientoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private EstanciaRepository estanciaRepository;

    public Vehiculo registrarVehiculo(String placa, String tipo) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipo);
        vehiculo.setTiempoAcumulado(0);
        return vehiculoRepository.save(vehiculo);
    }

    public Estancia registrarEntrada(String placa) {
        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findByPlaca(placa);
        Vehiculo vehiculo;

        if (vehiculoOpt.isPresent()) {
            vehiculo = vehiculoOpt.get();
        } else {
            vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setTipo("no_residente");
            vehiculo.setTiempoAcumulado(0);
            vehiculoRepository.save(vehiculo);
        }

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);
        estancia.setHoraEntrada(Calendar.getInstance());
        estancia = estanciaRepository.save(estancia);

        return estancia;
    }

    public Estancia registrarSalida(String placa) {
        Optional<Estancia> estanciaOpt = estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa);
        if (estanciaOpt.isEmpty()) {
            throw new PlacaNoEncontradaException("No se encontró una estancia en curso para este vehículo");
        }

        Estancia estancia = estanciaOpt.get();
        estancia.setHoraSalida(Calendar.getInstance());
        estanciaRepository.save(estancia);

        Vehiculo vehiculo = estancia.getVehiculo();
        int minutos = difEnMinutos(estancia.getHoraEntrada(), estancia.getHoraSalida());

        switch (vehiculo.getTipo()) {
            case "residente":
                vehiculo.setTiempoAcumulado(vehiculo.getTiempoAcumulado() + minutos);
                vehiculoRepository.save(vehiculo);
                break;
            case "no_residente":
                double importe = minutos * 0.5;
                System.out.println("Importe a pagar por " + minutos + " minutos: $" + importe);
                break;
            case "oficial":

                break;
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido");
        }

        return estancia;
    }

    private static int difEnMinutos(Calendar inicial, Calendar fin) {
        long milisDiff = fin.getTimeInMillis() - inicial.getTimeInMillis();
        return (int) TimeUnit.MILLISECONDS.toMinutes(milisDiff);
    }

    public void comenzarMes() {
        List<Vehiculo> vehiculos = vehiculoRepository.findAll();
        for (Vehiculo vehiculo : vehiculos) {
            if ("residente".equals(vehiculo.getTipo())) {
                vehiculo.setTiempoAcumulado(0);
            }
        }
        vehiculoRepository.saveAll(vehiculos);
        estanciaRepository.deleteAll();
    }

    public String generarInformePagosResidentes(String nombreArchivo) {
        List<Vehiculo> residentes = vehiculoRepository.findByTipo("residente");
        if (residentes.isEmpty()) {
            return "No hay vehículos residentes registrados.";
        }

        StringBuilder informe = new StringBuilder();
        informe.append("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

        for (Vehiculo vehiculo : residentes) {
            double cantidadAPagar = vehiculo.getTiempoAcumulado() * 0.05;
            informe.append(String.format("%s\t%d\t%.2f\n", vehiculo.getPlaca(), vehiculo.getTiempoAcumulado(), cantidadAPagar));
        }

        return informe.toString();
    }
}
