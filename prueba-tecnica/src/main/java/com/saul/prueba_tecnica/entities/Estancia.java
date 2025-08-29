package com.saul.prueba_tecnica.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "estancias")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date entrada;

    private Date salida;

    @ManyToOne
    @JoinColumn(name = "placa_vehiculo")
    private Vehiculo vehiculo;

    public Estancia() {
    }

    public Estancia(Long id, Date entrada, Date salida, Vehiculo vehiculo) {
        this.id = id;
        this.entrada = entrada;
        this.salida = salida;
        this.vehiculo = vehiculo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    
}
