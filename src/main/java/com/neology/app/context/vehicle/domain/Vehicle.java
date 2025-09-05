package com.neology.app.context.vehicle.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
public class Vehicle {

    @Id
    @Column(length = 15)
    @NotBlank
    @Size(max = 15)
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank
    private VehicleType type;

}
