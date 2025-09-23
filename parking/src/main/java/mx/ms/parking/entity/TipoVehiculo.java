package mx.ms.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "c_tipo_vehiculo",schema = "administration")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    // Relación con Vehiculo (uno a muchos)
    @OneToMany(mappedBy = "tipoVehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehiculo> vehiculos;
}
