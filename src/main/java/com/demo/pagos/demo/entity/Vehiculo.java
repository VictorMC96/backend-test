package com.demo.pagos.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehiculo {

    @Id
    private String placa;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estancia> estancias = new ArrayList<>();

    public Vehiculo(String placa) {
        this.placa = placa;
        this.estancias = new ArrayList<>();
    }

    public void agregarEstancia(Estancia estancia) {
        estancia.setVehiculo(this);
        estancias.add(estancia);
    }

    public abstract java.math.BigDecimal calcularPago(long minutosEstancia);

    public void limpiarEstancias() {
        estancias.clear();
    }
}

