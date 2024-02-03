package com.examen.oodoo.repository;

import com.examen.oodoo.entity.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia,Long>{
}
