package com.plh.parking.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Calendar;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ESTANCIA")
public class EstanciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private VehicleEntity vehicle;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar entryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar exitTime;

    private Integer durationMinutes;

    private BigDecimal pago;


}
