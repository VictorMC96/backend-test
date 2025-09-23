package com.estacionamiento.app.estacionamiento.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.estacionamiento.app.estacionamiento.entities.TiempoAcumulado;

public interface TiempoAcumuladoRepository extends CrudRepository<TiempoAcumulado, Long>{

	@Query("SELECT p FROM TiempoAcumulado p WHERE p.placa= :placa")
	TiempoAcumulado findByPlaca(@Param("placa") String placa);
	
	@Modifying
    @Transactional
	@Query("UPDATE TiempoAcumulado p SET p.minutos= :minutos, p.total= :total ")
	int updateTiempo(@Param("minutos")int minutos, @Param("total")float total);
	
	
}
