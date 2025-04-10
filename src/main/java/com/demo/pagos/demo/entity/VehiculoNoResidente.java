package com.demo.pagos.demo.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VehiculoNoResidente extends Vehiculo {

    @Builder
    public VehiculoNoResidente(String placa) {
        super(placa); // No envíes una lista
    }
    @Override
    public BigDecimal calcularPago(long minutosEstancia) {
        return BigDecimal.valueOf(minutosEstancia).multiply(BigDecimal.valueOf(0.5));
    }
}
