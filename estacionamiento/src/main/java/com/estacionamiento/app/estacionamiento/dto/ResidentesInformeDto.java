package com.estacionamiento.app.estacionamiento.dto;

import java.io.Serializable;

public class ResidentesInformeDto implements Serializable{
	
	private String placa;
	private Integer tiempo;
	private Float cantidad;
	
	public ResidentesInformeDto() {
		
	}
	
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public Integer getTiempo() {
		return tiempo;
	}
	public void setTiempo(Integer tiempo) {
		this.tiempo = tiempo;
	}
	public Float getCantidad() {
		return cantidad;
	}
	public void setCantidad(Float cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
