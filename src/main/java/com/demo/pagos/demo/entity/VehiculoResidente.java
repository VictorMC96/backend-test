package com.demo.pagos.demo.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VehiculoResidente extends Vehiculo {

    private long minutosAcumulados = 0;

    @Builder
    public VehiculoResidente(String placa) {
        super(placa);
    }

    @Override
    public BigDecimal calcularPago(long minutosEstancia) {
        minutosAcumulados += minutosEstancia;
        return BigDecimal.ZERO;
    }

    public BigDecimal calcularTotalPagar() {
        return BigDecimal.valueOf(minutosAcumulados).multiply(BigDecimal.valueOf(0.05));
    }

    public void reiniciarMes() {
        minutosAcumulados = 0;
        limpiarEstancias();
    }
}
