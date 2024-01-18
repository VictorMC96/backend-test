package com.neology.parkingneology.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;

@Data
@Entity
public class Parking {

    @Id
    private String plate;
    private Calendar entryHour;
    private Double cost;
    private int minutes;
    private Calendar exitHour;

}
