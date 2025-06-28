package com.gestion.pruebaTecnica.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehiculos")
public class Vehiculo implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idVehiculo;

        @NotEmpty
        @Column(name = "numero_placa")
        private String numeroPlaca;

        @Column(name = "fecha_entrada")
        private LocalDateTime fechaEntrada;

        @Column(name = "fecha_salida")
        private LocalDateTime fechaSalida;

        @Column(name = "tipo_vehiculo")
        private String vehiculo;

        @NotNull
        private double importe;

        private int tiempo;

        @OneToOne
        @JoinColumn(name = "id_vehiculo", referencedColumnName = "id_vehiculo" , insertable = true, updatable = true)
        private TipoVehiculo tipoVehiculo;

        public Vehiculo() {
        }

        public Vehiculo(Long idVehiculo, String numeroPlaca, LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String vehiculo, double importe, int tiempo, TipoVehiculo tipoVehiculo) {
                this.idVehiculo = idVehiculo;
                this.numeroPlaca = numeroPlaca;
                this.fechaEntrada = fechaEntrada;
                this.fechaSalida = fechaSalida;
                this.vehiculo = vehiculo;
                this.importe = importe;
                this.tiempo = tiempo;
                this.tipoVehiculo = tipoVehiculo;
        }

        public Long getIdVehiculo() {
                return idVehiculo;
        }

        public void setIdVehiculo(Long idVehiculo) {
                this.idVehiculo = idVehiculo;
        }

        public String getNumeroPlaca() {
                return numeroPlaca;
        }

        public void setNumeroPlaca(String numeroPlaca) {
                this.numeroPlaca = numeroPlaca;
        }

        public LocalDateTime getFechaEntrada() {
                return fechaEntrada;
        }

        public void setFechaEntrada(LocalDateTime fechaEntrada) {

                this.fechaEntrada = fechaEntrada;
        }

        public LocalDateTime getFechaSalida() {
                return fechaSalida;
        }

        public void setFechaSalida(LocalDateTime fechaSalida) {
                this.fechaSalida = fechaSalida;
        }

        public String getVehiculo() {
                return vehiculo;
        }

        public void setVehiculo(String vehiculo) {
                this.vehiculo = vehiculo;
        }

        public double getImporte() {
                return importe;
        }

        public void setImporte(double importe) {
                this.importe = importe;
        }

        public int getTiempo() {
                return tiempo;
        }

        public void setTiempo(int tiempo) {
                this.tiempo = tiempo;
        }

        public TipoVehiculo getTipoVehiculo() {
                return tipoVehiculo;
        }

        public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
                this.tipoVehiculo = tipoVehiculo;
        }

        @Override
        public String toString() {
                return "Vehiculo{" +
                        "idVehiculo=" + idVehiculo +
                        ", numeroPlaca='" + numeroPlaca + '\'' +
                        ", fechaEntrada=" + fechaEntrada +
                        ", fechaSalida=" + fechaSalida +
                        ", vehiculo='" + vehiculo + '\'' +
                        ", importe=" + importe +
                        ", tiempo=" + tiempo +
                        ", tipoVehiculo=" + tipoVehiculo +
                        '}';
        }
}