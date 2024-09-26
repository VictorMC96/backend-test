package com.backendtest.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Estancia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vehiculo vehiculo;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar entrada;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar salida;

}
