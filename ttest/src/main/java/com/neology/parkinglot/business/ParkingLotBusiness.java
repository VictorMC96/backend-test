package com.neology.parkinglot.business;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;


public interface ParkingLotBusiness {
	
	ResponseEntity<String> registerVehicleCheckIn(String request, HttpSession session);
	ResponseEntity<String> registerVehicleCheckOut(String request, HttpSession session);
	ResponseEntity<String> registerOfficialVehicle(String request, HttpSession session);
	ResponseEntity<String> registerResidentVehicle(String request, HttpSession session);
	ResponseEntity<String> resetMonth(String request, HttpSession session);
	ResponseEntity<String> residentPayment(String request, HttpSession session);
}
