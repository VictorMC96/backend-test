package com.accesoVehicular.neology.model;


import jakarta.persistence.*;

@Entity
@Table(name = "acumulado")
public class Acumulado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_acumulado;

    @ManyToOne
    @JoinColumn(name = "fk_id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "acumulado")
    private Long acumulado;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "reportado", nullable = false)
    private boolean reportado;

    public Acumulado() {
    }

    public Acumulado(Vehiculo vehiculo, Long acumulado){
        this.vehiculo = vehiculo;
        this.acumulado = acumulado;
        this.activo = true;
        this.reportado = false;
    }

    public Long getId_acumulado() {
        return id_acumulado;
    }

    public void setId_acumulado(Long id_acumulado) {
        this.id_acumulado = id_acumulado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Long getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(Long acumulado) {
        this.acumulado = acumulado;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isReportado() {
        return reportado;
    }

    public void setReportado(boolean reportado) {
        this.reportado = reportado;
    }
}
