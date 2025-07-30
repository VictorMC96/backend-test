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
import com.neology.parkinglot.entity.vehicles.ConstantEnum;
import com.neology.parkinglot.entity.vehicles.FormatTypeEnum;
import com.neology.parkinglot.entity.vehicles.Vehicle;
import com.neology.parkinglot.util.ReportGenerator;
import com.neology.parkinglot.util.VehicleUtil;

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
		
		parkingLotDao.saveVehicle(vehicle);
		
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
		
		VehicleUtil.processVehicle(vehicle.getType(), dateIn, dateIn, vehicle);
		
		parkingLotDao.saveVehicle(vehicle);
		
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
		
		registerVehicle(officialVehicleRequest.getVpNumber(),FormatTypeEnum.OFICIAL);
		
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
		
		registerVehicle(residentVehicleRequest.getVpNumber(),FormatTypeEnum.RESIDENT);
		
		return null;
	}

	@Override
	public ResponseEntity<String> resetMonth(String request, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void residentPayment(HttpSession session) {
		ReportGenerator.generarInformeResidentes("informe_residentes.txt");
	}
	
 private void registerVehicle(String plate, FormatTypeEnum type) {
	 Vehicle vehicle = new Vehicle();
	 vehicle.setCheckIn(new Date());
	 vehicle.setCheckOut(new Date());
	 vehicle.setTotal(0.0);
	 vehicle.setTotalTime(0);
	 vehicle.setVpNumber(plate);
	 vehicle.setType(type.toString());
	 parkingLotDao.saveVehicle(vehicle);
 }

}
