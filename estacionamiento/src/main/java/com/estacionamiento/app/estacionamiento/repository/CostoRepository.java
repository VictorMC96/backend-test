package com.estacionamiento.app.estacionamiento.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.estacionamiento.app.estacionamiento.entities.Costo;


public interface CostoRepository extends CrudRepository<Costo, Long>{
	
	@Query("SELECT p FROM Costo p WHERE p.tipo= :tipo")
	Costo findByTipo(@Param("tipo") String tipo);

}
