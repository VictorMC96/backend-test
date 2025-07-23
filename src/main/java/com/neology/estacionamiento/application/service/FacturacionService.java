package com.neology.estacionamiento.application.service;

import com.neology.estacionamiento.domain.model.Vehiculo;
import com.neology.estacionamiento.domain.model.VehiculoResidente;
import com.neology.estacionamiento.domain.model.TipoVehiculo;
import com.neology.estacionamiento.domain.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes y cálculos de facturación,
 * especialmente para vehículos residentes.
 */
@Service
public class FacturacionService implements IFacturacionService {

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public FacturacionService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Genera un reporte detallado de todos los vehículos residentes
     * con sus tiempos acumulados y deudas del mes
     */
    @Override
    public ReporteResidentes generarReporteResidentes() {
        List<Vehiculo> vehiculosResidentes = vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE);

        List<ItemReporteResidente> items = vehiculosResidentes.stream()
                .filter(v -> v instanceof VehiculoResidente)
                .map(v -> (VehiculoResidente) v)
                .map(residente -> new ItemReporteResidente(
                        residente.getPlaca(),
                        residente.getTiempoAcumuladoMes(),
                        residente.calcularDeudaMensual()
                )).collect(Collectors.toList());

        BigDecimal totalGeneral = items.stream()
                .map(ItemReporteResidente::getCantidadAPagar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteResidentes(items, totalGeneral, Calendar.getInstance().getTime()); // ✅ Usar Calendar
    }

    /**
     * Calcula la deuda específica de un residente
     */
    @Override
    public BigDecimal calcularDeudaResidente(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }

        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + placa));

        if (!(vehiculo instanceof VehiculoResidente)) {
            throw new IllegalArgumentException("El vehículo no es de tipo residente: " + placa);
        }

        VehiculoResidente residente = (VehiculoResidente) vehiculo;
        return residente.calcularDeudaMensual();
    }

    /**
     * Obtiene el resumen de facturación mensual
     */
    @Override
    public ResumenFacturacion obtenerResumenFacturacion() {
        List<Vehiculo> vehiculosResidentes = vehiculoRepository.findByTipo(TipoVehiculo.RESIDENTE);

        int totalResidentes = vehiculosResidentes.size();
        int residentesConDeuda = 0;
        BigDecimal totalRecaudacion = BigDecimal.ZERO;
        int totalMinutosAcumulados = 0;

        for (Vehiculo vehiculo : vehiculosResidentes) {
            if (vehiculo instanceof VehiculoResidente) {
                VehiculoResidente residente = (VehiculoResidente) vehiculo;
                BigDecimal deuda = residente.calcularDeudaMensual();

                if (deuda.compareTo(BigDecimal.ZERO) > 0) {
                    residentesConDeuda++;
                }

                totalRecaudacion = totalRecaudacion.add(deuda);
                totalMinutosAcumulados += residente.getTiempoAcumuladoMes();
            }
        }

        return new ResumenFacturacion(
                totalResidentes,
                residentesConDeuda,
                totalRecaudacion,
                totalMinutosAcumulados,
                Calendar.getInstance().getTime() // ✅ Usar Calendar
        );
    }

    /**
     * Clase para representar un item del reporte de residentes
     */
    public static class ItemReporteResidente {
        private final String placa;
        private final int minutosEstacionados;
        private final BigDecimal cantidadAPagar;

        public ItemReporteResidente(String placa, int minutosEstacionados, BigDecimal cantidadAPagar) {
            this.placa = placa;
            this.minutosEstacionados = minutosEstacionados;
            this.cantidadAPagar = cantidadAPagar;
        }

        // Getters
        public String getPlaca() { return placa; }
        public int getMinutosEstacionados() { return minutosEstacionados; }
        public BigDecimal getCantidadAPagar() { return cantidadAPagar; }

        // Para compatibilidad con versiones anteriores
        public BigDecimal getDeudaMensual() { return cantidadAPagar; }

        public String formatearLinea() {
            return String.format("%-10s\t%d\t\t%.2f", placa, minutosEstacionados, cantidadAPagar);
        }

        /**
         * Formatea la línea según el formato exacto requerido en el caso de uso
         */
        public String formatearLineaPersonalizada() {
            return String.format("%s\t%d\t\t%.2f", placa, minutosEstacionados, cantidadAPagar);
        }
    }

    /**
     * Clase para el reporte completo de residentes
     */
    public static class ReporteResidentes {
        private final List<ItemReporteResidente> items;
        private final BigDecimal totalGeneral;
        private final Date fechaGeneracion; // ✅ Cambiar a Date

        public ReporteResidentes(List<ItemReporteResidente> items, BigDecimal totalGeneral, Date fechaGeneracion) {
            this.items = items;
            this.totalGeneral = totalGeneral;
            this.fechaGeneracion = fechaGeneracion;
        }

        // Getters
        public List<ItemReporteResidente> getItems() { return items; }
        public BigDecimal getTotalGeneral() { return totalGeneral; }
        public Date getFechaGeneracion() { return fechaGeneracion; } // ✅ Cambiar a Date

        /**
         * Genera un reporte en formato texto para descarga
         */
        public String generarReporteTexto() {
            StringBuilder sb = new StringBuilder();
            sb.append("REPORTE DE FACTURACIÓN - RESIDENTES\n");
            sb.append("Fecha de generación: ").append(fechaGeneracion).append("\n\n");
            sb.append("Placa\t\tMinutos\t\tImporte\n");
            sb.append("========================================\n");

            for (ItemReporteResidente item : items) {
                sb.append(item.formatearLinea()).append("\n");
            }

            sb.append("========================================\n");
            sb.append("TOTAL GENERAL: $").append(totalGeneral).append("\n");

            return sb.toString();
        }

        /**
         * Genera un reporte en formato texto según el formato exacto del caso de uso
         * Formato: "Núm. placa \t Tiempo estacionado (min.) \t Cantidad a pagar"
         */
        public String generarReporteTextoPersonalizado() {
            StringBuilder sb = new StringBuilder();
            sb.append("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n");

            for (ItemReporteResidente item : items) {
                sb.append(item.formatearLineaPersonalizada()).append("\n");
            }

            return sb.toString();
        }
    }

    /**
     * Clase para el resumen de facturación
     */
    public static class ResumenFacturacion {
        private final int totalResidentes;
        private final int residentesConDeuda;
        private final BigDecimal totalRecaudacion;
        private final int totalMinutosAcumulados;
        private final Date fechaConsulta; // ✅ Cambiar a Date

        public ResumenFacturacion(int totalResidentes, int residentesConDeuda,
                                BigDecimal totalRecaudacion, int totalMinutosAcumulados, Date fechaConsulta) {
            this.totalResidentes = totalResidentes;
            this.residentesConDeuda = residentesConDeuda;
            this.totalRecaudacion = totalRecaudacion;
            this.totalMinutosAcumulados = totalMinutosAcumulados;
            this.fechaConsulta = fechaConsulta;
        }

        // Getters
        public int getTotalResidentes() { return totalResidentes; }
        public int getResidentesConDeuda() { return residentesConDeuda; }
        public BigDecimal getTotalRecaudacion() { return totalRecaudacion; }
        public int getTotalMinutosAcumulados() { return totalMinutosAcumulados; }
        public Date getFechaConsulta() { return fechaConsulta; } // ✅ Cambiar a Date
    }
} 