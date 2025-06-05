package com.neology.parkinglot.service;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neology.parkinglot.business.ParkingLotBusiness;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping(value="/api/parking/parkingapp")
public class ParkingService {
	
	
	private final ParkingLotBusiness parkingLotBusiness;
	ParkingService(ParkingLotBusiness parkingLotBusiness){
		this.parkingLotBusiness= parkingLotBusiness;
	}
	
	@PostMapping(value="registercheckin", consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> registerCheckIn(@RequestBody String request, HttpServletRequest httpServlet){
		return (ResponseEntity<String>) parkingLotBusiness.registerVehicleCheckIn(request, httpServlet.getSession(false));
	}
	
	@PostMapping(value="registercheckout", consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> registerCheckOut(@RequestBody String request, HttpServletRequest httpServlet){
		return (ResponseEntity<String>) parkingLotBusiness.registerVehicleCheckOut(request, httpServlet.getSession(false));
	}
	
	@PostMapping(value="registercheckout", consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> registerOfficialVehicle(@RequestBody String request, HttpServletRequest httpServlet){
		return (ResponseEntity<String>) parkingLotBusiness.registerOfficialVehicle(request, httpServlet.getSession(false));
	}
	
	@PostMapping(value="registercheckout", consumes=MediaType.TEXT_PLAIN_VALUE, produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> registerResidentVehicle(@RequestBody String request, HttpServletRequest httpServlet){
		return (ResponseEntity<String>) parkingLotBusiness.registerResidentVehicle(request, httpServlet.getSession(false));
	}
}
