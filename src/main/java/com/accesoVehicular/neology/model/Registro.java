package com.accesoVehicular.neology.model;

import java.math.BigDecimal;
import java.util.Calendar;
import jakarta.persistence.*;

@Entity
@Table(name = "registro")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_registro;

    @ManyToOne
    @JoinColumn(name = "fk_id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_hora_entrada", nullable = false)
    private Calendar fechaHoraEntrada;

    @Column(name = "fecha_hora_salida")
    private Calendar fechaHoraSalida;

    @Column(name = "importe")
    //Un double o un float deberian ser suficientes para representar el importe, pero BigDecimal es más preciso además
    // de que no tendria conflictos con importes muy muy grandes.
    private BigDecimal importe;


    public Registro() {
    }

    public Registro(Vehiculo vehiculo, Calendar fechaHoraEntrada) {
        this.vehiculo = vehiculo;
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public Long getId_registro() {
        return id_registro;
    }

    public void setId_registro(Long id_registro) {
        this.id_registro = id_registro;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Calendar getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(Calendar fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public Calendar getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Calendar fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}
