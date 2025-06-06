package com.prueba.estacionamiento.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoVehiculoConverter implements AttributeConverter<TipoVehiculo, String> {

    @Override
    public String convertToDatabaseColumn(TipoVehiculo tipo) {
        return tipo != null ? tipo.name() : null;
    }

    @Override
    public TipoVehiculo convertToEntityAttribute(String valor) {
        return valor != null ? TipoVehiculo.valueOf(valor) : null;
    }
}