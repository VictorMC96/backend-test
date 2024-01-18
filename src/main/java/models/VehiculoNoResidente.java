package models;

import jakarta.persistence.OneToMany;

import java.util.List;

public class VehiculoNoResidente extends Vehiculo{

    @OneToMany(mappedBy = "vehiculo")
    private List<Estancia> estancias;

    private int tiempoAcumulado =0;


    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }

    public int getTiempoAcumulado() {
        return tiempoAcumulado;
    }

    public void setTiempoAcumulado(int tiempoAcumulado) {
        this.tiempoAcumulado = tiempoAcumulado;
    }
}
