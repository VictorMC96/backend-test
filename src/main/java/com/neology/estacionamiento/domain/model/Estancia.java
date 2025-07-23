package com.neology.estacionamiento.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Entidad que representa una estancia de un vehículo en el estacionamiento.
 * Registra la entrada, salida y datos de facturación.
 */
@Entity
@Table(name = "estancias")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_entrada", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaEntrada;

    @Column(name = "fecha_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaSalida;

    @Column(name = "minutos_estancia")
    private Integer minutosEstancia;

    @Column(name = "importe_pagado", precision = 10, scale = 2)
    private BigDecimal importePagado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEstancia estado;

    // Constructor sin argumentos requerido por JPA
    protected Estancia() {
    }

    public Estancia(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.fechaEntrada = Calendar.getInstance(); // ✅ Usar Calendar como se especifica
        this.estado = EstadoEstancia.ACTIVA;
    }

    /**
     * Registra la salida del vehículo, calcula minutos de estancia e importe
     */
    public BigDecimal registrarSalida() {
        if (this.fechaSalida != null) {
            throw new IllegalStateException("La estancia ya tiene registrada una salida");
        }

        this.fechaSalida = Calendar.getInstance(); // ✅ Usar Calendar como se especifica
        this.minutosEstancia = difEnMinutos(fechaEntrada, fechaSalida); // ✅ Usar método especificado
        this.importePagado = vehiculo.calcularImporte(minutosEstancia);
        this.estado = EstadoEstancia.FINALIZADA;

        return this.importePagado;
    }

    /**
     * Calcula los minutos transcurridos desde la entrada hasta ahora
     */
    public int calcularMinutosTranscurridos() {
        Calendar ahora = fechaSalida != null ? fechaSalida : Calendar.getInstance();
        return difEnMinutos(fechaEntrada, ahora); // ✅ Usar método especificado
    }

    /**
     * Obtiene la diferencia en minutos entre dos fechas
     * @param inicial fecha inicial
     * @param fechaFinal fecha final
     * @return diferencia fechaFinal-inicial en minutos
     */
    private static int difEnMinutos(Calendar inicial, Calendar fechaFinal) {
        if (inicial == null || fechaFinal == null) {
            return 0;
        }
        
        long diferenciaMillis = fechaFinal.getTimeInMillis() - inicial.getTimeInMillis();
        return (int) (diferenciaMillis / (1000 * 60)); // Convertir de milisegundos a minutos
    }

    /**
     * Verifica si la estancia está activa
     */
    public boolean estaActiva() {
        return estado == EstadoEstancia.ACTIVA;
    }

    /**
     * Cancela la estancia (por ejemplo, en caso de error de registro)
     */
    public void cancelar() {
        this.estado = EstadoEstancia.CANCELADA;
    }

    // Getters y Setters
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

    public Calendar getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Calendar fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Calendar getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Calendar fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Integer getMinutosEstancia() {
        return minutosEstancia;
    }

    public void setMinutosEstancia(Integer minutosEstancia) {
        this.minutosEstancia = minutosEstancia;
    }

    public BigDecimal getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(BigDecimal importePagado) {
        this.importePagado = importePagado;
    }

    public EstadoEstancia getEstado() {
        return estado;
    }

    public void setEstado(EstadoEstancia estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estancia)) return false;
        Estancia estancia = (Estancia) o;
        return id != null && id.equals(estancia.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Estancia{" +
                "id=" + id +
                ", placa=" + (vehiculo != null ? vehiculo.getPlaca() : "null") +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", minutosEstancia=" + minutosEstancia +
                ", importePagado=" + importePagado +
                ", estado=" + estado +
                '}';
    }
} 