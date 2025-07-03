package com.neology.estacionamiento.domain;

import jakarta.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "estancias")
public class Estancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_placa")
    private Vehiculo vehiculo;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar entrada;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar salida;

    public Estancia() {}

    public Estancia(Calendar entrada) {
        this.entrada = entrada;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
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
