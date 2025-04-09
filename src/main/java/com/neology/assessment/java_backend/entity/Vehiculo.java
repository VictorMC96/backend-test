package com.neology.assessment.java_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;

@Entity
@Data
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column
    private Calendar fechaRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipoVehiculoId")
    private TipoVehiculo tipoVehiculo;



}
