package com.neology.assessment.java_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Data
public class Estancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Calendar fechaEntrada;

    @Column
    private Calendar fechaSalida;

    @Column
    private Integer tiempoMinutos;

    @Column
    private Double montoPagar;

    @Column
    private Boolean activo;

    @Column
    private Boolean mesActual;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vehiculoId")
    private Vehiculo vehiculo;


}
