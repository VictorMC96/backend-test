package com.headhunter.parking.Parking.service;

import com.headhunter.parking.Parking.constants.CatTypeVehicleENUM;
import com.headhunter.parking.Parking.dto.RespuestaServicioDto;

import java.time.LocalDateTime;

public interface ParkingService {
    public RespuestaServicioDto addVehicle(String numberPlate, CatTypeVehicleENUM typeVehicle, LocalDateTime dateInit, LocalDateTime dateExit);

    public RespuestaServicioDto generateFile(String fileName);

    public RespuestaServicioDto restartResident();


}
