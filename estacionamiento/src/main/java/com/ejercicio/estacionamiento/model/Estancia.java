package com.ejercicio.estacionamiento.model;


import jakarta.persistence.*;

import java.util.Calendar;

@Entity
public class Estancia {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id

    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar horaEntrada;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar horaSalida;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public Calendar getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(Calendar horaEntrada) { this.horaEntrada = horaEntrada; }
    public Calendar getHoraSalida() { return horaSalida; }
    public void setHoraSalida(Calendar horaSalida) { this.horaSalida = horaSalida; }
}
