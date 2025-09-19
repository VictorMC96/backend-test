package com.estacionamiento.parkingsystem.model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "vehiculos")

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "vehiculoTipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VehiculoOficial.class, name = "oficial"),
        @JsonSubTypes.Type(value = VehiculoResidente.class, name = "residente"),
        @JsonSubTypes.Type(value = VehiculoNoResidente.class, name = "no_residente")
})

public abstract class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "placa", unique = true, nullable = false, length = 20)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo",nullable = false, length = 20)
    private TipoVehiculo tipo;

    public Vehiculo() {
    }

    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

}
