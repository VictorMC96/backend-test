package com.prueba.estacionamiento.model;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Estancia")

public class Estancia {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 10, nullable = false)
	private String placa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Calendar entrada;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar salida;

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

	public Calendar getEntrada() {
		return entrada;
	}

	public void setEntrada(Calendar entrada) {
		this.entrada = entrada;
	}

	public Calendar getSalida() {
		return salida;
	}

	public void setSalida(Calendar salida) {
		this.salida = salida;
	}
	
	

}
