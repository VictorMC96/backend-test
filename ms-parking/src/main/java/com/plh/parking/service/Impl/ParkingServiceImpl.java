package com.plh.parking.service.Impl;

import com.plh.parking.commons.Utils;
import com.plh.parking.commons.enums.VehicleTypeEnum;
import com.plh.parking.commons.exception.ResourceNotFoundException;
import com.plh.parking.model.dto.StayResponseDto;
import com.plh.parking.model.mapper.GeneralMapper;
import com.plh.parking.persistence.entities.EstanciaEntity;
import com.plh.parking.persistence.entities.VehicleEntity;
import com.plh.parking.persistence.repository.EstanciaRepository;
import com.plh.parking.persistence.repository.VehicleRepository;
import com.plh.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RequiredArgsConstructor
@Service
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepository;
    private final EstanciaRepository estanciaRepository;
    private final GeneralMapper generalMapper;

    @Value("${parking.cost.minute}")
    private BigDecimal rateMinute;

    @Override
    public void registerEntry(String plate) {
        final VehicleEntity vehicle = vehicleRepository.findByPlate(plate)
                .orElseGet(() -> saveVehicle(plate, VehicleTypeEnum.NOT_RESIDENT));

        EstanciaEntity stay = new EstanciaEntity();
        stay.setVehicle(vehicle);
        stay.setEntryTime(Utils.dateNow());
        estanciaRepository.save(stay);
    }

    public VehicleEntity saveVehicle(String plate, VehicleTypeEnum vehicleType) {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setPlate(plate);
        vehicleEntity.setIdType(vehicleType.getId());
        return vehicleRepository.save(vehicleEntity);
    }

    @Override
    public BigDecimal registerExit(String plate) {
        VehicleEntity vehicle = vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Vehiculo no encontrado:" + plate));

        final EstanciaEntity stayEntity = estanciaRepository.findFirstByVehicleAndExitTimeIsNull(vehicle)
                .orElseThrow(() -> new ResourceNotFoundException("No hay estancia para: " + plate));

        Calendar exit = Utils.dateNow();
        stayEntity.setExitTime(exit);

        int minutes = Utils.diffMinute(stayEntity.getEntryTime(), exit);
        stayEntity.setDurationMinutes(minutes);

        processExit(vehicle, stayEntity);

        estanciaRepository.save(stayEntity);
        vehicleRepository.save(vehicle);

        return stayEntity.getPago();

    }


    public void processExit(VehicleEntity vehicleEntity, EstanciaEntity stayEntity) {
        stayEntity.setPago(BigDecimal.ZERO);
        int minutes = stayEntity.getDurationMinutes();

        if (VehicleTypeEnum.RESIDENT.getId() == vehicleEntity.getIdType()) {

            int newAccum = Optional.ofNullable(vehicleEntity.getTotalMinutes()).orElse(0) + minutes;
            vehicleEntity.setTotalMinutes(newAccum);
        } else if (VehicleTypeEnum.NOT_RESIDENT.getId() == vehicleEntity.getIdType()) {
            BigDecimal fee = rateMinute.multiply(BigDecimal.valueOf(minutes));
            stayEntity.setPago(fee);
        }

    }


    @Override
    public void beginsMonth() {
        estanciaRepository.deleteByIdType(VehicleTypeEnum.OFFICIAL.getId());
        vehicleRepository.updateMinuteByIdType(VehicleTypeEnum.RESIDENT.getId());
    }

    @Override
    public List<StayResponseDto> getStays() {
        return estanciaRepository.findAllFetch()
                .stream()
                .map(generalMapper::entityToDto)
                .toList();
    }

}
