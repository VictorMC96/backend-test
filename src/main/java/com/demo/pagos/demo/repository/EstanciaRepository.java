package com.demo.pagos.demo.repository;

import com.demo.pagos.demo.entity.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanciaRepository extends JpaRepository<Estancia, Long> {}