package com.ar21.pruebatecnica.entities;

import com.ar21.pruebatecnica.utils.TipoVehiculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    @Column(name="tipo_vehiculo")
    private String tipoVehiculo;

    @Column(name="fecha_registro ")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "vehiculo",orphanRemoval = true)
    private List<EntradaSalida> registros = new ArrayList<>();

}
