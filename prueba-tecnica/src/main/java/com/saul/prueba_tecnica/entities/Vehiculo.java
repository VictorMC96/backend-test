package com.saul.prueba_tecnica.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    private String placa;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;

    private int minutosAcumulados;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Estancia> estancias;

    public Vehiculo() {
    }

    public Vehiculo(String placa, TipoVehiculo tipo, int minutosAcumulados, List<Estancia> estancias) {
        this.placa = placa;
        this.tipo = tipo;
        this.minutosAcumulados = minutosAcumulados;
        this.estancias = estancias;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public int getMinutosAcumulados() {
        return minutosAcumulados;
    }

    public void setMinutosAcumulados(int minutosAcumulados) {
        this.minutosAcumulados = minutosAcumulados;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }

    
}
