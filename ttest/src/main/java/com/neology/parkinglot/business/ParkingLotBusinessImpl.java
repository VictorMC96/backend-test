package com.neology.parkinglot.business;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neology.parkinglot.dao.ParkingLotDao;
import com.neology.parkinglot.entity.dto.LicensePlateNumberDTO;
import com.neology.parkinglot.entity.vehicles.Vehicle;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParkingLotBusinessImpl implements ParkingLotBusiness{
	
	@Autowired
	ParkingLotDao parkingLotDao;

	@Override
	public ResponseEntity<String> registerVehicleCheckIn(String request, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		LicensePlateNumberDTO checkInRequest = new LicensePlateNumberDTO();
		
		try {
			checkInRequest = mapper.readValue(request, LicensePlateNumberDTO.class);
		} catch (Exception e) {
			e.getCause();
		}
		
		Vehicle vehicle = parkingLotDao.getCarByLicensePlate(checkInRequest.getVpNumber());
		
		Calendar dateIn;
		dateIn = Calendar.getInstance();
		Date date = dateIn.getTime();
		
		vehicle.setCheckIn(date);
		
		
		return new ResponseEntity<> ("OK",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> registerVehicleCheckOut(String request, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		LicensePlateNumberDTO checkOutRequest = new LicensePlateNumberDTO();
		
		try {
			checkOutRequest = mapper.readValue(request, LicensePlateNumberDTO.class);
		} catch (Exception e) {
			e.getCause();
		}
		
		Vehicle vehicle = parkingLotDao.getCarByLicensePlate(checkOutRequest.getVpNumber());
		
		Calendar dateIn;
		dateIn = Calendar.getInstance();
		Date date = dateIn.getTime();
		
		vehicle.setCheckOut(date);
		
		
		return new ResponseEntity<> ("OK",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> registerOfficialVehicle(String request, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		LicensePlateNumberDTO officialVehicleRequest = new LicensePlateNumberDTO();
		
		try {
			officialVehicleRequest = mapper.readValue(request, LicensePlateNumberDTO.class);
		} catch (Exception e) {
			e.getCause();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<String> registerResidentVehicle(String request, HttpSession session) {
		ObjectMapper mapper = new ObjectMapper();
		LicensePlateNumberDTO residentVehicleRequest = new LicensePlateNumberDTO();
		
		try {
			residentVehicleRequest = mapper.readValue(request, LicensePlateNumberDTO.class);
		} catch (Exception e) {
			e.getCause();
		}
		
		return null;
	}

	@Override
	public ResponseEntity<String> resetMonth(String request, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> residentPayment(String request, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

}
