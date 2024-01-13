package com.oscar.gestor.repository.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.Setter;


@Entity
@Table (name="lugar")
@Data
public class LugarEntity {
	private String placa;
	private Date fechaEntrada;
	private Date fechaSalida;

}
