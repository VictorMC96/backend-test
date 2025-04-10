package com.demo.pagos.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entrada;
    private LocalDateTime salida;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;
}