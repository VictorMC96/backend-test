package com.neology.parkingneology.model;

import com.neology.parkingneology.utils.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Vehicle {
    @Id
    private String plate;
    private Type type;
}
