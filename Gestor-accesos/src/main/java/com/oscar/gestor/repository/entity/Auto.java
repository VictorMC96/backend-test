package com.oscar.gestor.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name="Auto")
@Data	
public class Auto {
	private String placa;
	private Tipo tipo;
}


