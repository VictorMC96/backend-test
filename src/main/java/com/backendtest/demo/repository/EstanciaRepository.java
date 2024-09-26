package com.backendtest.demo.repository;

import com.backendtest.demo.entity.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {

}
