package com.neology.estacionamiento.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Clase base abstracta para todos los tipos de vehículos en el estacionamiento.
 * Utiliza herencia con discriminador para manejar diferentes tipos.
 */
@Entity
@Table(name = "vehiculos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_vehiculo", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehiculo {

    @Id
    @Column(name = "placa", length = 20)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoVehiculo tipo;

    @Column(name = "fecha_alta", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaAlta;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Estancia> estancias = new ArrayList<>();

    // Constructor sin argumentos requerido por JPA
    protected Vehiculo() {
    }

    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
        this.fechaAlta = Calendar.getInstance(); // ✅ Usar Calendar como se especifica
    }

    /**
     * Método abstracto para calcular el importe a pagar por minutos de estancia.
     * Cada tipo de vehículo implementa su propia lógica de cálculo.
     */
    public abstract BigDecimal calcularImporte(int minutos);

    /**
     * Agrega una nueva estancia al vehículo
     */
    public void agregarEstancia(Estancia estancia) {
        estancias.add(estancia);
        estancia.setVehiculo(this);
    }

    /**
     * Obtiene la estancia activa del vehículo (si existe)
     */
    public Estancia getEstanciaActiva() {
        return estancias.stream()
                .filter(Estancia::estaActiva)
                .findFirst()
                .orElse(null);
    }

    // Getters y Setters
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

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return placa != null && placa.equals(vehiculo.placa);
    }

    @Override
    public int hashCode() {
        return placa != null ? placa.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "placa='" + placa + '\'' +
                ", tipo=" + tipo +
                ", fechaAlta=" + fechaAlta +
                '}';
    }
} 