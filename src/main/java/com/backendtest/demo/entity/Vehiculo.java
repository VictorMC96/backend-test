package com.backendtest.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Vehiculo {
    @Id
    private String placa;

    @OneToMany(mappedBy = "vehiculo")
    private List<Estancia> estancias;



}
