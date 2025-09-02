package com.estacionamiento.parking.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {

    @Id
    @Column(length = 15)
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
    private ResidentAccount residentAccount;

}