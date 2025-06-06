package com.prueba.estacionamiento.model;

import java.util.Calendar;


public class TarifaRequest {
	private String placa;
    private TipoVehiculo tipo;
    private Calendar entrada;
    private Calendar salida;
    
    
    public TarifaRequest(){}
    
	public TarifaRequest(String placa, TipoVehiculo tipo, Calendar entrada, Calendar salida) {
		super();
		this.placa = placa;
		this.tipo = tipo;
		this.entrada = entrada;
		this.salida = salida;
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
