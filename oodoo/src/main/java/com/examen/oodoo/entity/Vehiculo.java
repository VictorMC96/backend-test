package com.examen.oodoo.entity;

import com.examen.oodoo.enums.TipoVehiculo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String placa;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipo;
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Estancia> estancias = new ArrayList<>();

    public Vehiculo() {
    }
    public Vehiculo(String placa, TipoVehiculo tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }
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

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }

    public Estancia getEstanciaActiva() {
        if (estancias != null) {
            for (Estancia estancia : estancias) {
                if (estancia.getHoraSalida() == null) {
                    return estancia;
                }
            }
        }
        return null;
    }
}
