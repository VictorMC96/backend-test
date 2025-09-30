package com.plh.parking.service;

import com.plh.parking.model.dto.StayResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ParkingService {

    void registerEntry(String plate);

    BigDecimal registerExit(String plate);


    void beginsMonth();


    List<StayResponseDto> getStays();

}
