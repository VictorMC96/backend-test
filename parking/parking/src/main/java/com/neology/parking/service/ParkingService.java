package com.neology.parking.service;

import com.neology.parking.model.*;
import com.neology.parking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {
	
    private static final BigDecimal RATE_RESIDENT_PER_MIN = new BigDecimal("0.05");
    private static final BigDecimal RATE_NON_RESIDENT_PER_MIN = new BigDecimal("0.5");

    private final VehicleRepository vehicleRepository;
    private final StayRepository stayRepository;

    public ParkingService(VehicleRepository vehicleRepository, StayRepository stayRepository) {
        this.vehicleRepository = vehicleRepository;
        this.stayRepository = stayRepository;
    }

    //@Transactional
    public void addOfficial(String plate) {
        vehicleRepository.findByPlate(plate).ifPresentOrElse(v -> {
            v.setType(VehicleType.OFICIAL);
            vehicleRepository.save(v);
        }, () -> {
            vehicleRepository.save(new Vehicle(plate, VehicleType.OFICIAL));
        });
    }

    @Transactional
    public void addResident(String plate) {
        vehicleRepository.findByPlate(plate).ifPresentOrElse(v -> {
            v.setType(VehicleType.RESIDENTE);
            if (v.getAccumulatedMinutes() == null) v.setAccumulatedMinutes(0);
            vehicleRepository.save(v);
        }, () -> {
            vehicleRepository.save(new Vehicle(plate, VehicleType.RESIDENTE));
        });
    }

    @Transactional
    public void registerEntry(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlate(plate)
                .orElseGet(() -> {
                    Vehicle v = new Vehicle(plate, VehicleType.NO_RESIDENTE);
                    return vehicleRepository.save(v);
                });

        // check if there's an open stay -> optional: prevent double entry
        Optional<Stay> open = stayRepository.findByVehicle_PlateAndExitTimeIsNull(plate);
        if (open.isPresent()) {
            throw new IllegalStateException("Ya existe una estancia abierta para la placa " + plate);
        }

        Calendar now = Calendar.getInstance();
        Stay stay = new Stay(vehicle, now);
        stay.setVehicle(vehicle);
        stayRepository.save(stay);
    }

    @Transactional
    public ExitResult registerExit(String plate) {
        Stay stay = stayRepository.findByVehicle_PlateAndExitTimeIsNull(plate)
                .orElseThrow(() -> new IllegalArgumentException("No existe una entrada abierta para la placa " + plate));

        Calendar now = Calendar.getInstance();
        int minutes = difEnMinutos(stay.getEntryTime(), now);

        stay.setExitTime(now);
        stay.setDurationMinutes(minutes);

        Vehicle vehicle = stay.getVehicle();
        if (vehicle.getType() == VehicleType.OFICIAL) {
            // guardar stay (no cobro)
            stayRepository.save(stay);
            return new ExitResult(plate, vehicle.getType(), minutes, BigDecimal.ZERO);
        } else if (vehicle.getType() == VehicleType.RESIDENTE) {
            // acumular minutos
            Integer acc = vehicle.getAccumulatedMinutes();
            if (acc == null) acc = 0;
            vehicle.setAccumulatedMinutes(acc + minutes);
            vehicleRepository.save(vehicle);
            stayRepository.save(stay);
            return new ExitResult(plate, vehicle.getType(), minutes, BigDecimal.ZERO);
        } else { // NO_RESIDENTE
            BigDecimal amount = RATE_NON_RESIDENT_PER_MIN.multiply(new BigDecimal(minutes));
            stay.setAmountPaid(amount);
            stayRepository.save(stay);
            return new ExitResult(plate, vehicle.getType(), minutes, amount);
        }
    }

    @Transactional
    public void beginMonth() {
        // Eliminar estancias registradas en coches oficiales
        // Nota: "elimina las estancias registradas en los coches oficiales" -> se interpreta como borrar stays de vehículos OFICIAL
        // alternativa: poder archivarlas. Aquí las eliminamos:
        List<Vehicle> officials = vehicleRepository.findAll()
                .stream()
                .filter(v -> v.getType() == VehicleType.OFICIAL)
                .toList();

        for (Vehicle v : officials) {
            // borrar estancias relacionadas
            v.getStays().clear();
            vehicleRepository.save(v);
        }

        // Poner a cero tiempo estacionado por residentes
        List<Vehicle> residents = vehicleRepository.findAll()
                .stream()
                .filter(v -> v.getType() == VehicleType.RESIDENTE)
                .toList();

        for (Vehicle r : residents) {
            r.setAccumulatedMinutes(0);
            vehicleRepository.save(r);
        }
    }

    // Genera informe TSV (tab-separated) en la ruta /tmp por ejemplo
    @Transactional(readOnly = true)
    public String generateResidentsReport(String filename) {
        List<Vehicle> residents = vehicleRepository.findAll()
                .stream()
                .filter(v -> v.getType() == VehicleType.RESIDENTE)
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

        for (Vehicle r : residents) {
            int minutes = r.getAccumulatedMinutes() == null ? 0 : r.getAccumulatedMinutes();
            BigDecimal amount = RATE_RESIDENT_PER_MIN.multiply(new BigDecimal(minutes)).setScale(2);
            sb.append(r.getPlate()).append("\t")
              .append(minutes).append("\t")
              .append(amount).append("\n");
        }

        // Guardar en file system (ejemplo en /tmp)
        java.nio.file.Path path = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"), filename);
        try {
            java.nio.file.Files.writeString(path, sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("No fue posible escribir el informe: " + e.getMessage(), e);
        }
        return path.toString();
    }

    // Método proporcionado para calcular diferencia en minutos entre dos Calendar
    private static int difEnMinutos(Calendar inicial, Calendar fin) {
        long millis = fin.getTimeInMillis() - inicial.getTimeInMillis();
        long minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millis);
        return (int) minutes;
    }

    // DTO de respuesta
    public static class ExitResult {
        public final String plate;
        public final VehicleType type;
        public final int minutes;
        public final BigDecimal amount;

        public ExitResult(String plate, VehicleType type, int minutes, BigDecimal amount) {
            this.plate = plate;
            this.type = type;
            this.minutes = minutes;
            this.amount = amount;
        }
    }
}
