package com.gestion.pruebaTecnica.servicio;

import com.gestion.pruebaTecnica.entidades.Vehiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehiculoService {

	public List<Vehiculo> findAll();
	public Page<Vehiculo> findAll(Pageable pageable);
	public void save(Vehiculo vehiculo);
	public Vehiculo findOne(Long id);
	public void delete(Long id);
	public Vehiculo existe(String placa);

}