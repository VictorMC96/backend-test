package com.neology.estacionamiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Calendar;

@Entity
@Data
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "placa")
    private Vehiculo vehiculo;

    private Calendar horaEntrada;
    private Calendar horaSalida;

    public Estancia() {
        this.horaEntrada = Calendar.getInstance();
    }

}
