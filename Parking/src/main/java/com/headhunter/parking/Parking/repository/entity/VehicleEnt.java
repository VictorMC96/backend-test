package com.headhunter.parking.Parking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "VEHICLE")
public class VehicleEnt {

    @Id
    @Column(name = "ID_VEHICLE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short idVehicle;

    @Column(name = "NUMBER_PLATE")
    private String numberPlate;

    @Column(name = "DATE_INIT")
    private LocalDateTime dateInit;

    @Column(name = "DATE_EXIT")
    private LocalDateTime dateExit;

    @Column(name = "TIME_ACUM")
    private Integer timeAcum;

    @Column(name = "TO_PAY")
    private Double toPay;

    @ManyToOne
    @JoinColumn(name = "ID_TYPE", referencedColumnName = "ID_TYPE")
    private CatTypeVehicle idType;


}
