package com.plh.parking.commons.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Optional;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum VehicleTypeEnum {

    OFFICIAL(1, "OFICIAL"),
    RESIDENT(2, "RESIDENTE"),
    NOT_RESIDENT(3, "NO_RESIDENTE");

    private Integer id;
    private String type;

    public static Optional<VehicleTypeEnum> of(String type) {
        return Stream.of(VehicleTypeEnum.values())
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .findAny();
    }

    public static Optional<VehicleTypeEnum> byId(Integer id) {
        return Stream.of(VehicleTypeEnum.values())
                .filter(v -> v.getId().equals(id))
                .findAny();
    }


}
