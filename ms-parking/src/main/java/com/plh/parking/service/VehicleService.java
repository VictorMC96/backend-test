package com.plh.parking.service;

import com.plh.parking.model.dto.VehicleDto;
import com.plh.parking.model.dto.VehicleResponseDto;

import java.util.List;

public interface VehicleService {

    void saveVehicle(VehicleDto vehicleDto);

    List<VehicleResponseDto> findAll();
}
