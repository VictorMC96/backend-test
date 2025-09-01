package com.estacionamiento.parking.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resident_accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResidentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int accumulatedMinutes;

    @OneToOne
    @JoinColumn(name = "vehicle_plate", nullable = false, unique = true)
    @JsonBackReference
    private Vehicle vehicle;

}