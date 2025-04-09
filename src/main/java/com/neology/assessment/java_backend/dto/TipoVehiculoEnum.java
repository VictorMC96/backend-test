package com.neology.assessment.java_backend.dto;

import lombok.Getter;
import lombok.Setter;

public enum TipoVehiculoEnum {
    RESIDENTE(1L,"Vehiculo residente"),
    OFICIAL(2L, "Vehiculo oficial"),
    NO_RESIDENTE(3L, "Vehiculo no residente");

    @Getter
    private final Long codigo;
    @Setter
    private final String descripcion;

    TipoVehiculoEnum(Long codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public static TipoVehiculoEnum getTipoVehiculoEnum(int codigo){
        for(TipoVehiculoEnum tipoVehiculo : TipoVehiculoEnum.values()){
            if(tipoVehiculo.getCodigo() == codigo){
                return tipoVehiculo;
            }
        }
        return null;
    }
}
