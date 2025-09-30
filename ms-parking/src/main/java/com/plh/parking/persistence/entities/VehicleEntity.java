package com.plh.parking.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "VEHICLE")
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plate;

    @Column(nullable = false)
    private Integer idType;

    // Acumulado del mes
    // reiniciar al incio del mes y mandar a un historico(vehiculo,pagoMes)
    private Integer totalMinutes = 0;

//    @ColumnDefault("true
//    protected Boolean enable

    @OneToMany(mappedBy = "vehicle")
    private List<EstanciaEntity> estancias = new ArrayList<>();

}
