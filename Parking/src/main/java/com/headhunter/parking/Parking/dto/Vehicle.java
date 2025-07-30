package com.headhunter.parking.Parking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Vehicle {

    private String numberPlate;
    private String type;
    private LocalDateTime dateInit;
    private LocalDateTime dateFin;

}
