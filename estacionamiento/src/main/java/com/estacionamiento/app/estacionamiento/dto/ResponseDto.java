package com.estacionamiento.app.estacionamiento.dto;

import java.io.Serializable;

public class ResponseDto implements Serializable{

	private String msg;
	private String horaEntrada;
	private String horaSalida;
	private String total;

	public ResponseDto () {
		
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getHoraEntrada() {
		return horaEntrada;
	}
	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	public String getHoraSalida() {
		return horaSalida;
	}
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	
	
}
