package com.example.abrahamtest.Parking_management.service;

public interface ParkingService {

    void registerOfficialVehicle(String licensePlate);
    void registerResidentVehicle(String licensePlate);
    void registerEntry(String licensePlate);
    void registerExit(String licensePlate);
    void startNewMonth();
    String generateResidentPaymentReport();
}
