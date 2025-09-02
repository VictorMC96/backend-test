package com.estacionamiento.parking.service;

import com.estacionamiento.parking.domain.*;
import com.estacionamiento.parking.pricing.*;
import com.estacionamiento.parking.repository.*;
import com.estacionamiento.parking.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingService {

    private final VehicleRepository vehicleRepo;
    private final ParkingSessionRepository sessionRepo;
    private final ResidentAccountRepository residentRepo;

    public ParkingSession registerEntry(String plate) {
        Optional<ParkingSession> open = sessionRepo.findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(plate);
        if (open.isPresent()) throw new IllegalStateException("Ya existe una entrada sin salida para " + plate);

        Vehicle v = vehicleRepo.findById(plate).orElse(null);

        ParkingSession ps = ParkingSession.builder()
                .plate(plate)
                .registeredType(v != null ? v.getType() : null)
                .entryTime(DateUtils.now())
                .open(true)
                .build();
        return sessionRepo.save(ps);
    }

    public ExitResult registerExit(String plate) {
        ParkingSession session = sessionRepo.findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(plate)
                .orElseThrow(() -> new IllegalStateException("No hay entrada abierta para " + plate));

        Calendar exit = DateUtils.now();
        int minutes = DateUtils.difEnMinutos(session.getEntryTime(), exit);
        session.setExitTime(exit);
        session.setOpen(false);
        sessionRepo.save(session);

        Vehicle vehicle = vehicleRepo.findById(plate).orElse(null);

        PricingStrategy strategy = pricingFor(vehicle);
        double amount = strategy.amountForMinutes(minutes);
        strategy.onExitSideEffects(plate, minutes);

        return new ExitResult(plate, minutes, amount);
    }

    private PricingStrategy pricingFor(Vehicle v) {
        if (v == null) return new NonResidentPricing();
        return switch (v.getType()) {
            case OFFICIAL -> new OfficialPricing();
            case RESIDENT -> new ResidentPricing(vehicleRepo, residentRepo);
        };
    }

    public Vehicle registerOfficial(String plate) {
        Vehicle v = Vehicle.builder().plate(plate).type(VehicleType.OFFICIAL).build();
        return vehicleRepo.save(v);
    }

    public Vehicle registerResident(String plate) {
        Vehicle v = Vehicle.builder().plate(plate).type(VehicleType.RESIDENT).build();
        v = vehicleRepo.save(v);
        ResidentAccount account = ResidentAccount.builder().vehicle(v).accumulatedMinutes(0).build();
        residentRepo.save(account);
        v.setResidentAccount(account);
        return v;
    }

    public void startMonth() {
        sessionRepo.deleteByRegisteredType(VehicleType.OFFICIAL);
        residentRepo.findAll().forEach(a -> a.setAccumulatedMinutes(0));
        residentRepo.flush();
    }

    public String generateResidentReport(String filePath) throws Exception {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar");
            List<ResidentAccount> accounts = residentRepo.findAll();
            for (ResidentAccount a : accounts) {
                String plate = a.getVehicle().getPlate();
                int minutes = a.getAccumulatedMinutes();
                double amount = ResidentPricing.amountForAccumulatedMinutes(minutes);
                out.printf("%s\t%d\t%.2f%n", plate, minutes, amount);
            }
        }
        return filePath;
    }

    public record ExitResult(String plate, int minutes, double amountMXN) {}
}
