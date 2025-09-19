package com.mx.estacionamiento.estacinamiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mx.estacionamiento.estacinamiento.model.Stay;

public interface StayRepository extends JpaRepository<Stay, Long> {

}
