package com.example.abrahamtest.Parking_management.service;

import com.example.abrahamtest.Parking_management.model.*;
import com.example.abrahamtest.Parking_management.repository.ParkingRecordRepository;
import com.example.abrahamtest.Parking_management.repository.VehicleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@Service
public class ParkingServiceImpl implements ParkingService{
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ParkingRecordRepository parkingRecordRepository;
    @Override
    public void registerOfficialVehicle(String licensePlate) {
        if (!vehicleRepository.existsById(licensePlate)) {
            OfficialVehicle vehicle = new OfficialVehicle(licensePlate);
            vehicleRepository.save(vehicle);
        }

    }

    @Override
    @Transactional
    public void registerResidentVehicle(String licensePlate) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(licensePlate);
        Vehicle vehicle;

        if (vehicleOpt.isEmpty()) {
            // Si no existe, se crea como no residente por defecto
            vehicle = new NonResidentVehicle(licensePlate);
            vehicleRepository.save(vehicle);
        } else {
            vehicle = vehicleOpt.get();
        }

        // Verificar si ya tiene un registro de entrada sin salida
        List<ParkingRecord> activeRecords = parkingRecordRepository.findByVehicleAndExitTimeIsNull(vehicle);
        if (activeRecords.isEmpty()) {
            ParkingRecord record = new ParkingRecord(vehicle, Calendar.getInstance());
            parkingRecordRepository.save(record);
        }

    }

    @Override
    @Transactional

    public void registerEntry(String licensePlate) {

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(licensePlate);
        Vehicle vehicle;

        if (vehicleOpt.isEmpty()) {
            vehicle = new NonResidentVehicle(licensePlate);
            vehicleRepository.save(vehicle);
        } else {
            vehicle = vehicleOpt.get();
        }

        List<ParkingRecord> activeRecords = parkingRecordRepository.findByVehicleAndExitTimeIsNull(vehicle);
        if (activeRecords.isEmpty()) {
            Calendar now = Calendar.getInstance();
            ParkingRecord record = new ParkingRecord(vehicle, now);
            parkingRecordRepository.save(record);
        }

    }

    @Override
    @Transactional
    public void registerExit(String licensePlate) {

        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(licensePlate);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();

            List<ParkingRecord> activeRecords = parkingRecordRepository.findByVehicleAndExitTimeIsNull(vehicle);
            if (!activeRecords.isEmpty()) {
                ParkingRecord record = activeRecords.get(0);
                record.setExitTime(Calendar.getInstance());

                long parkedMinutes = record.getParkedMinutes();

                if (vehicle instanceof ResidentVehicle) {
                    ((ResidentVehicle) vehicle).addParkedMinutes(parkedMinutes);
                    vehicleRepository.save(vehicle);
                }

                parkingRecordRepository.save(record);
            }
        }

    }

    @Override
    @Transactional
    public void startNewMonth() {
        // Reset resident vehicles
        List<ResidentVehicle> residentVehicles = vehicleRepository.findAll()
                .stream()
                .filter(v -> v instanceof ResidentVehicle)
                .map(v -> (ResidentVehicle) v)
                .toList();

        for (ResidentVehicle vehicle : residentVehicles) {
            vehicle.resetParkedMinutes();
            vehicleRepository.save(vehicle);
        }

        // Borrar official vehicle records
        List<OfficialVehicle> officialVehicles = vehicleRepository.findAll()
                .stream()
                .filter(v -> v instanceof OfficialVehicle)
                .map(v -> (OfficialVehicle) v)
                .toList();

        for (OfficialVehicle vehicle : officialVehicles) {
            List<ParkingRecord> records = parkingRecordRepository.findByVehicle(vehicle);
            parkingRecordRepository.deleteAll(records);
        }

    }

    @Override
    @Transactional(readOnly = true)

    public String generateResidentPaymentReport() {
        StringBuilder report = new StringBuilder();
        report.append("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

        List<ResidentVehicle> residentVehicles = vehicleRepository.findAll()
                .stream()
                .filter(v -> v instanceof ResidentVehicle)
                .map(v -> (ResidentVehicle) v)
                .toList();

        for (ResidentVehicle vehicle : residentVehicles) {
            double payment = vehicle.calculatePayment(vehicle.getTotalParkedMinutes());
            report.append(String.format("%s\t\t%d\t\t\t%.2f\n",
                    vehicle.getLicensePlate(),
                    vehicle.getTotalParkedMinutes(),
                    payment));
        }

        return report.toString();
    }

}
