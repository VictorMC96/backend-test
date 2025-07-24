package com.neology.parking.repository;

import com.neology.parking.model.entity.Stay;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByPlateNumberAndExitTimeIsNull(String plateNumber);
    List<Stay> findByPlateNumber(String plateNumber);
}
