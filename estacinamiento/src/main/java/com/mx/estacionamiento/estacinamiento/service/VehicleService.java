package com.mx.estacionamiento.estacinamiento.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.estacionamiento.estacinamiento.dto.PaymentReportDTO;
import com.mx.estacionamiento.estacinamiento.model.Stay;
import com.mx.estacionamiento.estacinamiento.model.Vehicle;
import com.mx.estacionamiento.estacinamiento.model.VehicleType;
import com.mx.estacionamiento.estacinamiento.repository.StayRepository;
import com.mx.estacionamiento.estacinamiento.repository.VehicleRepository;
import com.mx.estacionamiento.estacinamiento.util.DateUtils;

@Service

public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final StayRepository stayRepository;

    public VehicleService(VehicleRepository vehicleRepository, StayRepository stayRepository) {
        this.vehicleRepository = vehicleRepository;
        this.stayRepository = stayRepository;
    }

    public Vehicle registerVehicle(String plate, VehicleType type) {
        Vehicle v = new Vehicle();
        v.setPlate(plate);
        v.setType(type);
        return vehicleRepository.save(v);
    }

    public Stay registerEntry(String plate) {
        Vehicle v = vehicleRepository.findById(plate)
                .orElseThrow(() -> new RuntimeException("Vehículo no registrado"));
        Stay s = new Stay();
        s.setVehicle(v);
        s.setEntryTime(Calendar.getInstance());
        return stayRepository.save(s);
    }

    public double registerExit(String plate) {
        Vehicle v = vehicleRepository.findById(plate)
                .orElseThrow(() -> new RuntimeException("Vehículo no registrado"));

        Stay s = v.getStays().stream()
                .filter(stay -> stay.getExitTime() == null)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay estancia abierta"));

        s.setExitTime(Calendar.getInstance());
        stayRepository.save(s);

        int minutes = DateUtils.diffInMinutes(s.getEntryTime(), s.getExitTime());

        switch (v.getType()) {
            case OFICIAL -> {
                return 0;
            }
            case RESIDENTE -> {
                v.setAccumulatedMinutes(v.getAccumulatedMinutes() + minutes);
                vehicleRepository.save(v);
                return 0;
            }
            case NO_RESIDENTE -> {
                return minutes * 0.5;
            }
            default ->
                throw new RuntimeException("Tipo desconocido");
        }
    }

    public List<PaymentReportDTO> generateResidentReport() {
        List<Vehicle> residents = vehicleRepository.findAll().stream()
                .filter(v -> v.getType() == VehicleType.RESIDENTE)
                .toList();

        return residents.stream()
                .map(v -> new PaymentReportDTO(
                v.getPlate(),
                v.getAccumulatedMinutes(),
                v.getAccumulatedMinutes() * 0.05
        ))
                .toList();
    }

    public void resetMonth() {

        vehicleRepository.findAll().forEach(v -> {
            if (v.getType() == VehicleType.OFICIAL) {
                v.getStays().clear();
            }
            if (v.getType() == VehicleType.RESIDENTE) {
                v.setAccumulatedMinutes(0);
            }
            vehicleRepository.save(v);
        });
    }

}
