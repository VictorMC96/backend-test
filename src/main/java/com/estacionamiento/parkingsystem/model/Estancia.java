package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estancias")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime horaEntrada;

    private LocalDateTime horaSalida;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    public long getDuracionEnMinutos(){
        if (horaEntrada != null && horaSalida != null){
            return java.time.Duration.between(horaEntrada, horaSalida).toMinutes();
        }
        return 0;
    }


}
