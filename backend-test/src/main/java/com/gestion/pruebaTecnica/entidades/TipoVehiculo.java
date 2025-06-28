package com.gestion.pruebaTecnica.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipo_vehiculo")
public class TipoVehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;

    @Column(name = "numero_placa")
    private String numeroPlaca;

    @Column(name = "vehiculo")
    private String vehiculoTipo;

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public TipoVehiculo() {
    }

    public TipoVehiculo(Long idVehiculo, String vehiculoTipo, String numeroPlaca) {
        this.idVehiculo = idVehiculo;
        this.vehiculoTipo = vehiculoTipo;
        this.numeroPlaca = numeroPlaca;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getNumeroPlaca() {
        return numeroPlaca;
    }

    public void setNumeroPlaca(String numeroPlaca) {
        this.numeroPlaca = numeroPlaca;
    }

    public String getVehiculoTipo() {
        return vehiculoTipo;
    }

    public void setVehiculoTipo(String vehiculoTipo) {
        this.vehiculoTipo = vehiculoTipo;
    }

    @Override
    public String toString() {
        return "TipoVehiculo{" +
                "idVehiculo=" + idVehiculo +
                ", numeroPlaca='" + numeroPlaca + '\'' +
                ", vehiculoTipo='" + vehiculoTipo + '\'' +
                '}';
    }
}