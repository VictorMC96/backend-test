package com.neology.estacionamiento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Vehiculo {

    @Id
    private String placa;

    private Calendar horaEntrada;
    private Calendar horaSalida;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Estancia> estancias = new ArrayList<>();
}
