package models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class VehiculoOficial extends Vehiculo{

    @OneToMany(mappedBy = "vehiculo")
    private List<Estancia> estancias;


    public List<Estancia> getEstancias() {
        return estancias;
    }

    public void setEstancias(List<Estancia> estancias) {
        this.estancias = estancias;
    }
}
