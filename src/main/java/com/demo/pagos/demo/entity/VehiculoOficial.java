package com.demo.pagos.demo.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VehiculoOficial extends Vehiculo {

    @Builder
    public VehiculoOficial(String placa) {
        super(placa);
    }

    @Override
    public BigDecimal calcularPago(long minutosEstancia) {
        return BigDecimal.ZERO;
    }
}
