package com.daniel_lopez_rivera.backend_test.service;

import com.daniel_lopez_rivera.backend_test.exception.ResourceNotFoundException;
import com.daniel_lopez_rivera.backend_test.model.Estancia;
import com.daniel_lopez_rivera.backend_test.model.Vehiculo;
import com.daniel_lopez_rivera.backend_test.repository.EstanciaRepository;
import com.daniel_lopez_rivera.backend_test.repository.VehiculoRepository;
import com.daniel_lopez_rivera.backend_test.util.TimeUtils;
import com.daniel_lopez_rivera.backend_test.util.VehiculoTipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionamientoService {

    @Autowired
    private VehiculoRepository vehiculoRepo;

    @Autowired
    private EstanciaRepository estanciaRepo;

    private static final double FACTOR_DE_COBRO_RESIDENTES = 0.05;
    private static final double FACTOR_DE_COBRO_COMUN = 0.5;

    private final String PATH_BASE_REPORTES = "C:/reportes-estacionamiento";

    @Transactional
    public void agregarVehiculo(String plate, VehiculoTipo type) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(plate);
        vehiculo.setTipo(type);
        vehiculoRepo.save(vehiculo);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> consultarVehiculos() {
        return vehiculoRepo.findAll();
    }

    @Transactional
    public void registrarEntrada(String placa) {
        Optional<Vehiculo> optionalVehiculo = vehiculoRepo.findById(placa);
        if (optionalVehiculo.isPresent()){
            Vehiculo vehiculo = optionalVehiculo.get();
            vehiculo.setFechaAccesoEstacionamiento(Calendar.getInstance());
            vehiculoRepo.save(vehiculo);
        } else {
            throw new ResourceNotFoundException("Placa no registrada");
        }
    }

    @Transactional
    public String registrarSalida(String placa) {
        Optional<Vehiculo> optionalVehiculo = vehiculoRepo.findById(placa);
        if (optionalVehiculo.isPresent()){
            Vehiculo vehiculo = optionalVehiculo.get();
            Calendar fechaSalida = Calendar.getInstance();
            Calendar fechaEntrada = vehiculo.getFechaAccesoEstacionamiento();

            int minutos = TimeUtils.difEnMinutos(fechaEntrada, fechaSalida);
            String result = "";

            switch (vehiculo.getTipo()) {
                case OFICIAL -> {
                    Estancia estancia = new Estancia();
                    estancia.setVehiculo(vehiculo);
                    estancia.setFechaEntrada(fechaEntrada);
                    estancia.setFechaSalida(fechaSalida);
                    estanciaRepo.save(estancia);
                    result = "Estancia registrada";
                }
                case RESIDENTE -> {
                    vehiculo.setTotalMinutosEstacionado(vehiculo.getTotalMinutosEstacionado() + minutos);
                    result = "Minutos acumulados: " + vehiculo.getTotalMinutosEstacionado();
                }
                case NO_RESIDENTE -> {
                    double amount = minutos * this.FACTOR_DE_COBRO_COMUN;
                    result = "Debe pagar: MXN$" + amount;
                }
            }

            vehiculo.setFechaAccesoEstacionamiento(null);
            vehiculoRepo.save(vehiculo);
            return result;
        } else {
            throw new ResourceNotFoundException("Placa no registrada");
        }
    }

    @Transactional
    public void iniciarNuevoMes() {
        estanciaRepo.deleteAll();
        List<Vehiculo> residentes = vehiculoRepo.findByTipo(VehiculoTipo.RESIDENTE);
        for (Vehiculo v : residentes) {
            v.setTotalMinutosEstacionado(0);
        }
        vehiculoRepo.saveAll(residentes);
    }

    @Transactional(readOnly = true)
    public boolean generarReporte(String nombreFichero) {
        List<Vehiculo> residentes = vehiculoRepo.findByTipo(VehiculoTipo.RESIDENTE);
        File rutaDirectorio = Paths.get(PATH_BASE_REPORTES).toFile();
        Path rutaFichero = Paths.get(PATH_BASE_REPORTES,nombreFichero + ".txt");
        if (!rutaDirectorio.exists()){
            rutaDirectorio.mkdirs();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rutaFichero.toString()));
            writer.write("Núm. placa \tTiempo estacionado (min.) \tCantidad a pagar \n");
            for (Vehiculo v : residentes) {
                double amount = v.getTotalMinutosEstacionado() * FACTOR_DE_COBRO_RESIDENTES;
                writer.write(v.getPlaca() + " \t\t\t\t "
                        + v.getTotalMinutosEstacionado() + " min "
                        + "\t\t\t\t\t MXN$" + amount + "\n");
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
