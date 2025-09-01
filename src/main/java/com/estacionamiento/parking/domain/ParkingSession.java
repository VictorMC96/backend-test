package com.estacionamiento.parking.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Table(name = "parking_sessions",
        indexes = { @Index(name="idx_plate_open", columnList = "plate, open") })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String plate;

    @Enumerated(EnumType.STRING)
    private VehicleType registeredType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Calendar entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar exitTime;

    @Column(nullable = false)
    private boolean open;
}
