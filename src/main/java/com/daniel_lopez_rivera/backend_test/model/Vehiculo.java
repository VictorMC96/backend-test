package com.daniel_lopez_rivera.backend_test.model;

import com.daniel_lopez_rivera.backend_test.util.VehiculoTipo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Data
public class Vehiculo {
    @Id
    private String placa;

    @Enumerated(EnumType.STRING)
    private VehiculoTipo tipo;

    private int totalMinutosEstacionado = 0;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Estancia> estancias = new ArrayList<>();

    private Calendar fechaAccesoEstacionamiento;
}
