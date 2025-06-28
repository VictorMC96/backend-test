package com.gestion.pruebaTecnica.repositorios;

import com.gestion.pruebaTecnica.entidades.Vehiculo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VehiculoRepository extends PagingAndSortingRepository<Vehiculo, Long>{

    @Query(value = "FROM Vehiculo t WHERE t.numeroPlaca =:placa")
    Vehiculo countExiste(String placa);

    @Modifying
    @Query("update Vehiculo u set u.fechaSalida = ?1 where u.numeroPlaca = ?2")
    Vehiculo salida(String fechaSalida, String placa);

}