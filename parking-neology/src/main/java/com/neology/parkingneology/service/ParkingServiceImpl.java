package com.neology.parkingneology.service;

import com.neology.parkingneology.model.Parking;
import com.neology.parkingneology.model.Vehicle;
import com.neology.parkingneology.repository.ParkingRepository;
import com.neology.parkingneology.repository.VehicleRepository;
import com.neology.parkingneology.utils.Type;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ParkingServiceImpl implements ParkingService {
    private ParkingRepository parkingRepository;
    private VehicleRepository vehicleRepository;

    @Override
    public Parking registerEntry(String plate) {
        Optional<Vehicle> vehicle = vehicleRepository.getVehicleByPlate(plate);
        if (vehicle.isPresent()) {
            Optional<Parking> parking = parkingRepository.getParkingByPlate(plate);
            if (parking.isPresent()) {
                parking.get().setEntryHour(Calendar.getInstance());
                parkingRepository.save(parking.get());
                return parking.get();
            }
            Parking newParking = new Parking();
            newParking.setPlate(plate);
            newParking.setEntryHour(Calendar.getInstance());
            if (vehicle.get().getType().equals(Type.RESIDENT)) {
                newParking.setCost(0.05);
                newParking.setMinutes(0);
            }
            parkingRepository.save(newParking);
            return newParking;

        }else {
            Vehicle newVehicle = new Vehicle();
            Parking parking = new Parking();
            newVehicle.setPlate(plate);
            newVehicle.setType(Type.VISITOR);
            parking.setPlate(plate);
            parking.setEntryHour(Calendar.getInstance());
            parking.setCost(0.5);
            parking.setMinutes(0);
            vehicleRepository.save(newVehicle);
            parkingRepository.save(parking);
            return parking;
        }
    }

    private int calculateMinutes(Calendar entryHour, Calendar exitHour) {
        int minutes = 0;
        minutes += (exitHour.get(Calendar.HOUR_OF_DAY) - entryHour.get(Calendar.HOUR_OF_DAY)) * 60;
        minutes += exitHour.get(Calendar.MINUTE) - entryHour.get(Calendar.MINUTE);
        return minutes;
    }

    @Override
    public Parking registerExit(String plate) {
        Optional<Parking> parking = parkingRepository.getParkingByPlate(plate);
        Optional<Vehicle> vehicle = vehicleRepository.getVehicleByPlate(plate);
        if (!parking.isPresent() || !vehicle.isPresent()){
            throw new RuntimeException("No existe el vehiculo");
        }
        if (vehicle.get().getType().equals(Type.OFFICIAL)) {
            parking.get().setExitHour(Calendar.getInstance());
            parkingRepository.save(parking.get());
            return parking.get();
        } else if (vehicle.get().getType().equals(Type.VISITOR)) {
            parking.get().setMinutes(calculateMinutes(parking.get().getEntryHour(), Calendar.getInstance()));
            parkingRepository.delete(parking.get());
            return parking.get();
        } else {
            parking.get().setMinutes(calculateMinutes(parking.get().getEntryHour(), Calendar.getInstance()));
            parking.get().setExitHour(Calendar.getInstance());
            parkingRepository.save(parking.get());
            return parking.get();
        }
    }

    @Override
    public Vehicle registerOfficial(String plate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setType(Type.OFFICIAL);
        vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle registerResident(String plate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setType(Type.RESIDENT);
        vehicleRepository.save(vehicle);
        return vehicle;
    }

    @Override
    public List<Parking> startMonth() {
        List<Vehicle> vehicles = (List<Vehicle>) vehicleRepository.getAllVehicleByType(Type.OFFICIAL);
        for (Vehicle vehicle : vehicles) {
            parkingRepository.delete(parkingRepository.getParkingByPlate(vehicle.getPlate()).get());
        }
        List<Vehicle> vehiclesR = (List<Vehicle>) vehicleRepository.getAllVehicleByType(Type.RESIDENT);
        for (Vehicle vehicle : vehiclesR) {
            Optional<Parking> parking = parkingRepository.getParkingByPlate(vehicle.getPlate());
            if (parking.isPresent()) {
                parking.get().setMinutes(0);
                parking.get().setEntryHour(Calendar.getInstance());
                parkingRepository.save(parking.get());
            }
        }

        return (List<Parking>) parkingRepository.findAll();
    }

    @Override
    public String pay(String fileName) {
        List<Parking> parkingsL = new ArrayList<>();
        List<Vehicle> vehicles = (List<Vehicle>) vehicleRepository.getAllVehicleByType(Type.RESIDENT);
        for (Vehicle vehicle: vehicles){
            Optional<Parking> parking = parkingRepository.getParkingByPlate(vehicle.getPlate());
            if (parking.isPresent()){
                parkingsL.add(parking.get());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Núm. placa \tTiempo estacionado (min.) \tCantidad a pagar\n");
        for (Parking parking : parkingsL) {
            sb.append(parking.getPlate() + "\t\t\t" + parking.getMinutes() + "\t\t\t\t\t\t" + parking.getCost() * parking.getMinutes() + "\n");
        }
        return sb.toString();
    }
}
