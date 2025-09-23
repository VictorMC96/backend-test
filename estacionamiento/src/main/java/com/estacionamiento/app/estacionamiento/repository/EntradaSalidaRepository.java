package com.estacionamiento.app.estacionamiento.repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;

public interface EntradaSalidaRepository extends CrudRepository<RegistroEntradaSalida,Long>{
    
	@Query("SELECT p FROM RegistroEntradaSalida p WHERE p.placa= :placa and activo=true")
	RegistroEntradaSalida findByPlaca(@Param("placa") String placa);
	
	@Modifying
    @Transactional
	@Query("DELETE FROM RegistroEntradaSalida p WHERE p.tipo= :tipo")
	void deleteRegistros(@Param("tipo") String tipo);
	
}
