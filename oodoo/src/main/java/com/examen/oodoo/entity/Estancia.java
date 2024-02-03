package com.examen.oodoo.entity;

import jakarta.persistence.*;

import java.util.Calendar;
@Entity
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    private Calendar horaEntrada;
    private Calendar horaSalida;

    public Estancia() {
    }
    public Estancia(Vehiculo vehiculo, Calendar horaEntrada) {
        this.vehiculo = vehiculo;
        this.horaEntrada = horaEntrada;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    public Calendar getHoraEntrada() {
        return horaEntrada;
    }
    public void setHoraEntrada(Calendar horaEntrada) {
        this.horaEntrada = horaEntrada;
    }
    public Calendar getHoraSalida() {
        return horaSalida;
    }
    public void setHoraSalida(Calendar horaSalida) {
        this.horaSalida = horaSalida;
    }
}
