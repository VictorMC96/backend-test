package com.estacionamiento.parkingsystem.model;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "vehiculos")
public abstract class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "placa", unique = true, nullable = false, length = 20)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo",nullable = false, length = 20)
    private TipoVehiculo tipo;

    // Constructor vacío
    public Vehiculo() {
    }

    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

}
