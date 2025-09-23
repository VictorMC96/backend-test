package mx.ms.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "d_vehiculo",schema = "administration")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", length = 15, nullable = false, unique = true)
    private String placa;
    @Column(name = "id_tipo_vehiculo")
    private Long idTipoVehiculo;
    @Column(name = "tiempo_total_acumulado")
    private int tiempoTotalAcumulado;
    // Relación con TipoVehiculo (muchos a uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_vehiculo", insertable = false, updatable = false)
    private TipoVehiculo tipoVehiculo;

    // Relación con Acceso (uno a muchos)
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Acceso> accesos;
}
