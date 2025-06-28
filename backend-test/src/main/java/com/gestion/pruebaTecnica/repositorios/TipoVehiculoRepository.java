package com.gestion.pruebaTecnica.repositorios;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TipoVehiculoRepository extends PagingAndSortingRepository<TipoVehiculo, Long> {

    @Query(value = "FROM TipoVehiculo t WHERE t.numeroPlaca =:placa")
    TipoVehiculo countExiste(String placa);

}