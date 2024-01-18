package com.neology.parkingneology.service;

import com.neology.parkingneology.model.Parking;
import com.neology.parkingneology.model.Vehicle;

import java.util.List;

public interface ParkingService {

    Parking registerEntry(String plate);

    Parking registerExit(String plate);

    Vehicle registerOfficial(String plate);

    Vehicle registerResident(String plate);

    List<Parking> startMonth();

    String pay(String fileName);
}
