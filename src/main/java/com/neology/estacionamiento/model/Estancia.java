package com.neology.estacionamiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Calendar;

@Entity
@Data
public class Estancia {

    @Id
    @GeneratedValue
    private Long id;

    private Calendar entrada;
    private Calendar salida;

    @ManyToOne
    private Vehiculo vehiculo;
}
