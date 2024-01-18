package models;

import jakarta.persistence.*;

import java.util.List;


public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int vehiculoId;

    String placa;

    String tipo;

    @OneToMany(mappedBy = "vehiculo")
    List<Estancia> estancias;

    //int tiempoAcumulado;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /*public int getTiempoAcumulado() {
        return tiempoAcumulado;
    }

    public void setTiempoAcumulado(int tiempoAcumulado) {
        this.tiempoAcumulado = tiempoAcumulado;
    }*/

    public int getId() {
        return vehiculoId;
    }

    public void setId(int id) {
        vehiculoId = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }
}
