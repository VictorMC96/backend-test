package com.ejercicio.estacionamiento.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Vehiculo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id


    private Long id;
    private String placa;
    private String tipo;
    private long tiempoAcumulado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getTiempoAcumulado() {
        return tiempoAcumulado;
    }

    public void setTiempoAcumulado(long tiempoAcumulado) {
        this.tiempoAcumulado = tiempoAcumulado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return tiempoAcumulado == vehiculo.tiempoAcumulado && Objects.equals(id, vehiculo.id) && Objects.equals(placa, vehiculo.placa) && Objects.equals(tipo, vehiculo.tipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa, tipo, tiempoAcumulado);
    }
}