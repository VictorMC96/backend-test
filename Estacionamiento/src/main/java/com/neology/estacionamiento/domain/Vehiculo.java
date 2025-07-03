package com.neology.estacionamiento.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

	@Id
	private String placa;

	@Enumerated(EnumType.STRING)
	private TipoVehiculo tipo;

	private int tiempoAcumulado;

	@OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
	private List<Estancia> estancias = new ArrayList<>();

	public Vehiculo() {
	}

	public Vehiculo(String placa, TipoVehiculo tipo) {
		this.placa = placa;
		this.tipo = tipo;
		this.tiempoAcumulado = 0;
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

	public int getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	public void setTiempoAcumulado(int tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	public List<Estancia> getEstancias() {
		return estancias;
	}

	public void setEstancias(List<Estancia> estancias) {
		this.estancias = estancias;
	}

	public void agregarEstancia(Estancia e) {
		estancias.add(e);
		e.setVehiculo(this);
	}

	public void resetAcumulado() {
		this.tiempoAcumulado = 0;
	}

	public void sumarTiempo(int minutos) {
		this.tiempoAcumulado += minutos;
	}
}
