package com.neology.estacionamiento.domain.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Implementación concreta para vehículos de residentes.
 * Los residentes pagan a final de mes a razón de $0.05 por minuto.
 */
@Entity
@DiscriminatorValue("RESIDENTE")
public class VehiculoResidente extends Vehiculo {

    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.05");

    @Column(name = "tiempo_acumulado_mes")
    private int tiempoAcumuladoMes = 0;

    // Constructor sin argumentos requerido por JPA
    protected VehiculoResidente() {
        super();
    }

    public VehiculoResidente(String placa) {
        super(placa, TipoVehiculo.RESIDENTE);
    }

    /**
     * Los residentes no pagan por estancia individual, sino mensualmente.
     * El cálculo de importe se usa solo para la facturación mensual.
     */
    @Override
    public BigDecimal calcularImporte(int minutos) {
        // Para registrar en la estancia, pero el residente no paga al salir
        return BigDecimal.ZERO;
    }

    /**
     * Calcula el importe basado en la tarifa de residentes: $0.05 por minuto
     * Este método se usa para la facturación mensual, no por estancia.
     */
    public BigDecimal calcularImporteMensual(int minutos) {
        return TARIFA_POR_MINUTO.multiply(new BigDecimal(minutos));
    }

    /**
     * Suma minutos al tiempo acumulado del mes actual
     */
    public void acumularTiempoMes(int minutos) {
        this.tiempoAcumuladoMes += minutos;
    }

    /**
     * Calcula la deuda total del mes basada en el tiempo acumulado
     */
    public BigDecimal calcularDeudaMensual() {
        return calcularImporteMensual(tiempoAcumuladoMes);
    }

    /**
     * Reinicia el tiempo acumulado al comenzar un nuevo mes
     */
    public void reiniciarTiempoMes() {
        this.tiempoAcumuladoMes = 0;
    }

    // Getters y Setters
    public int getTiempoAcumuladoMes() {
        return tiempoAcumuladoMes;
    }

    public void setTiempoAcumuladoMes(int tiempoAcumuladoMes) {
        this.tiempoAcumuladoMes = tiempoAcumuladoMes;
    }

    public static BigDecimal getTarifaPorMinuto() {
        return TARIFA_POR_MINUTO;
    }

    @Override
    public String toString() {
        return "VehiculoResidente{" +
                "placa='" + getPlaca() + '\'' +
                ", tiempoAcumuladoMes=" + tiempoAcumuladoMes +
                ", fechaAlta=" + getFechaAlta() +
                '}';
    }
} 