package com.estacionamiento.app.estacionamiento.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tiempo_acumulado")
public class TiempoAcumulado implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String placa;
	private Integer minutos;
	private Float costo;
    private Float total;
    
    public TiempoAcumulado() {
    	
    }
    
	public Long getId() {
		return id;
	}

	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public Integer getMinutos() {
		return minutos;
	}
	public void setMinutos(Integer minutos) {
		this.minutos = minutos;
	}
	public Float getCosto() {
		return costo;
	}
	public void setCosto(Float costo) {
		this.costo = costo;
	}
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
    
    
}
