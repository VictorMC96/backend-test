package mx.ms.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Calendar;

@Entity
@Table(name = "d_acceso",schema = "administration")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)  // mapear a DATE en PostgreSQL
    @Column(name = "fh_entrada", nullable = false)
    private Calendar fhEntrada;
    @Temporal(TemporalType.DATE)  // puede ser null
    @Column(name = "fh_salida")
    private Calendar fhSalida;
    @Column(name = "id_vehiculo")
    private Long idVehiculo;
    @Column(name = "tiempo_total")
    private int tiempoTotal;
    @Column(name = "importe")
    private Double importe;
    // Relación con Vehiculo (muchos a uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo", insertable = false, updatable = false)
    private Vehiculo vehiculo;
}
