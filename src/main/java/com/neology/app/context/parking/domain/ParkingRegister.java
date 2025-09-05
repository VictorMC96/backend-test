package com.neology.app.context.parking.domain;

import java.util.Calendar;

import com.neology.app.context.vehicle.domain.VehicleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parking_register")
@NoArgsConstructor
@Setter
public class ParkingRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String plate;

    @Enumerated(EnumType.STRING)
    private VehicleType registeredType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar entryAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar exitAt;

    @Column(nullable = false)
    private boolean isOpen;

    public ParkingRegister(String plate, VehicleType registeredType, Calendar entryAt, Calendar exitAt,
            boolean isOpen) {
        this.plate = plate;
        this.registeredType = registeredType;
        this.entryAt = entryAt;
        this.exitAt = exitAt;
        this.isOpen = isOpen;
    }

}
