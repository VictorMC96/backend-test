package com.prueba.estacionamiento.model;

import java.math.BigDecimal;
import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "pagos")
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String placa;

	@Column(name = "tipo")
	@Convert(converter = TipoVehiculoConverter.class)
	private TipoVehiculo tipo;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaPago;

	@Column(nullable = false)
	private BigDecimal monto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public TipoVehiculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoVehiculo tipo) {
		this.tipo = tipo;
	}

	public Calendar getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Calendar fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	
	

}
