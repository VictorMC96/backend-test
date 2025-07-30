package com.headhunter.parking.Parking.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "CAT_TYPE_VEHICLE")
public class CatTypeVehicle {

    @Id
    @Column(name = "ID_TYPE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idType;

    @Column(name = "CODE", nullable = false, length = 200)
    private String code;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

}
