package com.plh.parking.model.mapper;

import com.plh.parking.commons.enums.VehicleTypeEnum;
import com.plh.parking.model.dto.StayResponseDto;
import com.plh.parking.model.dto.VehicleResponseDto;
import com.plh.parking.persistence.entities.EstanciaEntity;
import com.plh.parking.persistence.entities.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneralMapper {

    @Mapping(source = "entity.vehicle.plate", target = "plate")
    StayResponseDto entityToDto(EstanciaEntity entity);


    @Mapping(source = "entity.idType", target = "type", qualifiedByName = "entityToResponsePlate")
    VehicleResponseDto entityToDto(VehicleEntity entity);

    @Named("entityToResponsePlate")
    default String entityToResponsePlate(Integer idType) {
        return VehicleTypeEnum.byId(idType).map(VehicleTypeEnum::getType).orElse(null);
    }


}
