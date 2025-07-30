package com.accesoVehicular.neology.model;

import jakarta.persistence.*;


@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_vehiculo;

    @Column(nullable = false, unique = true)
    private String placa;

    @ManyToOne
    @JoinColumn(name = "fk_id_tipo_vehiculo")
    private TipoVehiculo tipoVehiculo;

    @Column(nullable = false)
    private boolean activo;

    public Vehiculo() {
    }

    public Vehiculo(String placa, TipoVehiculo tipoVehiculo, boolean activo) {
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.activo = activo;
    }

    public Vehiculo(Long id_vehiculo, String placa, TipoVehiculo tipoVehiculo) {
        this.id_vehiculo = id_vehiculo;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
    }

    public Long getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(Long id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
