package com.accesoVehicular.neology.model;



import jakarta.persistence.*;


@Entity
@Table(name = "tipo_vehiculo")
public class TipoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipoVehiculo;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false)
    private double tarifa;

    public TipoVehiculo() {
    }

    public TipoVehiculo(Long id_tipoVehiculo, String nombre, boolean activo, double tarifa) {
        this.id_tipoVehiculo = id_tipoVehiculo;
        this.nombre = nombre;
        this.activo = activo;
        this.tarifa = tarifa;
    }

    public Long getId_tipoVehiculo() {
        return id_tipoVehiculo;
    }

    public void setId_tipoVehiculo(Long id_tipoVehiculo) {
        this.id_tipoVehiculo = id_tipoVehiculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}
