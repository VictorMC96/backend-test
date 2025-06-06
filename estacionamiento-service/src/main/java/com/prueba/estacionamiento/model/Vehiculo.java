package com.prueba.estacionamiento.model;




import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Vehiculo")
public class Vehiculo {
	@Id
	private String placa;

	@Column(name = "tipo")
	@Convert(converter = TipoVehiculoConverter.class)
	private TipoVehiculo tipo;
	
	
	private Long tiempoAcumulados;


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


	public Long getTiempoAcumulados() {
		return tiempoAcumulados;
	}


	public void setTiempoAcumulados(Long tiempoAcumulados) {
		this.tiempoAcumulados = tiempoAcumulados;
	}
	
	

}
