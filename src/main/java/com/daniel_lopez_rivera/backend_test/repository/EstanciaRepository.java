package com.daniel_lopez_rivera.backend_test.repository;

import com.daniel_lopez_rivera.backend_test.model.Estancia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
}