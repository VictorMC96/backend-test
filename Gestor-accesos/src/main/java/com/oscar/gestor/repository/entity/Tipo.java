package com.oscar.gestor.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table (name="Auto")
@Data
public class Tipo {
	
	@Id
	private int id;
	private String descripcion;

}
