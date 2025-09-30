package com.plh.parking.service.Impl;

import com.plh.parking.commons.enums.VehicleTypeEnum;
import com.plh.parking.commons.exception.ResourceNotFoundException;
import com.plh.parking.model.dto.VehicleDto;
import com.plh.parking.model.dto.VehicleResponseDto;
import com.plh.parking.model.mapper.GeneralMapper;
import com.plh.parking.persistence.entities.VehicleEntity;
import com.plh.parking.persistence.repository.VehicleRepository;
import com.plh.parking.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final GeneralMapper generalMapper;

    @Override
    public void saveVehicle(VehicleDto vehicleDto) {
        final VehicleTypeEnum vehicleType = VehicleTypeEnum.of(vehicleDto.type())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el tipo de vehiculo"));

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setPlate(vehicleDto.plate());
        vehicleEntity.setIdType(vehicleType.getId());

        vehicleRepository.save(vehicleEntity);
    }

    @Override
    public List<VehicleResponseDto> findAll() {
        return vehicleRepository.findAll()
                .stream().map(generalMapper::entityToDto)
                .toList();

    }


}
