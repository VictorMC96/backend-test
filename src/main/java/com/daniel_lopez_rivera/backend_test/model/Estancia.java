package com.daniel_lopez_rivera.backend_test.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Calendar;

@Entity
@Data
public class Estancia {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Vehiculo vehiculo;

    private Calendar fechaEntrada;
    private Calendar fechaSalida;
}
