package com.example.abrahamtest.Parking_management.model;

import com.example.abrahamtest.Parking_management.util.DateTool;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Getter
@Setter
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Vehicle vehicle;


    @Temporal(TemporalType.TIMESTAMP)
    private Calendar entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar exitTime;

    public ParkingRecord() {
    }

    public ParkingRecord(Vehicle vehicle, Calendar entryTime) {
        this.vehicle = vehicle;
        this.entryTime = entryTime;
    }

    public long getParkedMinutes() {
        if (exitTime == null || entryTime == null) {
            return 0;
        }

        long minutos = DateTool.difEnMinutos(entryTime, exitTime);

        return minutos;


    }
}
