package com.neology.parking.service;

import com.neology.parking.model.entity.*;
import com.neology.parking.repository.StayRepository;
import com.neology.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StayService {
    private final StayRepository stayRepository;
    private final VehicleRepository vehicleRepository;

    public StayService(StayRepository stayRepository, VehicleRepository vehicleRepository) {
        this.stayRepository = stayRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Stay registerEntry(String plateNumber) {
        Stay stay = new Stay(plateNumber, LocalDateTime.now());
        return stayRepository.save(stay);
    }

    public Optional<Double> registerExit(String plateNumber) {
        List<Stay> stays = stayRepository.findByPlateNumberAndExitTimeIsNull(plateNumber);
        if (stays.isEmpty()) return Optional.empty();

        Stay stay = stays.get(0);
        stay.setExitTime(LocalDateTime.now());
        stayRepository.save(stay);

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(plateNumber);
        if (!vehicleOpt.isPresent()) return Optional.empty();

        Vehicle v = vehicleOpt.get();
        long minutes = Duration.between(stay.getEntryTime(), stay.getExitTime()).toMinutes();

        if (v instanceof OfficialVehicle) return Optional.of(0.0);
        if (v instanceof ResidentVehicle) {
            ((ResidentVehicle) v).setAccumulatedMinutes(((ResidentVehicle) v).getAccumulatedMinutes() + (int) minutes);
            vehicleRepository.save(v);
            return Optional.of(0.0);
        }
        return Optional.of(minutes * 0.5);
    }

    public void clearStays() {
        stayRepository.deleteAll();
    }

    public List<Stay> getAllByPlate(String plate) {
        return stayRepository.findByPlateNumber(plate);
    }
}
