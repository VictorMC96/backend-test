package com.demo.pagos.demo.services;

import com.demo.pagos.demo.entity.*;
import com.demo.pagos.demo.repository.EstanciaRepository;
import com.demo.pagos.demo.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionamientoService {

    private final VehiculoRepository vehiculoRepo;
    private final EstanciaRepository estanciaRepo;

    @Autowired
    public EstacionamientoService(VehiculoRepository vehiculoRepo, EstanciaRepository estanciaRepo) {
        this.vehiculoRepo = vehiculoRepo;
        this.estanciaRepo = estanciaRepo;
    }

    public void registrarEntrada(String placa) {
        Vehiculo vehiculo = vehiculoRepo.findById(placa).isPresent() ? vehiculoRepo.findById(placa).get() : new VehiculoNoResidente(placa);
        Estancia estancia = new Estancia();
        estancia.setEntrada(LocalDateTime.now());
        estancia.setVehiculo(vehiculo);
        vehiculo.agregarEstancia(estancia);

        vehiculoRepo.save(vehiculo);
    }
    public BigDecimal registrarSalida(String placa) {
        Optional<Vehiculo> optionalVehiculo = vehiculoRepo.findById(placa);
        if (!optionalVehiculo.isPresent()) {
            throw new RuntimeException("Vehículo no encontrado");
        }

        Vehiculo vehiculo = optionalVehiculo.get();

        Estancia estancia = null;
        for (Estancia e : vehiculo.getEstancias()) {
            if (e.getSalida() == null) {
                estancia = e;
                break;
            }
        }

        if (estancia == null) throw new RuntimeException("Estancia no iniciada");

        estancia.setSalida(LocalDateTime.now());
        estanciaRepo.save(estancia);

        long minutos = Duration.between(estancia.getEntrada(), estancia.getSalida()).toMinutes();
        return vehiculo.calcularPago(minutos);
    }

    public void altaOficial(String placa) {
        vehiculoRepo.save(new VehiculoOficial(placa));
    }

    public void altaResidente(String placa) {
        vehiculoRepo.save(new VehiculoResidente(placa));
    }

    public void comenzarMes() {
        List<Vehiculo> vehiculos = vehiculoRepo.findAll();
        for (Vehiculo v : vehiculos) {
            if (v instanceof VehiculoOficial) {
                v.limpiarEstancias();
            } else if (v instanceof VehiculoResidente) {
                ((VehiculoResidente) v).reiniciarMes();
            }
            vehiculoRepo.save(v);
        }
    }

    public void generarInforme(String nombreArchivo) throws IOException {
        List<Vehiculo> vehiculos = vehiculoRepo.findAll();
        BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
        writer.write("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

        for (Vehiculo v : vehiculos) {
            if (v instanceof VehiculoResidente) {
                VehiculoResidente r = (VehiculoResidente) v;
                writer.write(String.format("%s\t%d\t%.2f\n", r.getPlaca(), r.getMinutosAcumulados(), r.calcularTotalPagar()));
            }
        }
        writer.close();
    }

}
