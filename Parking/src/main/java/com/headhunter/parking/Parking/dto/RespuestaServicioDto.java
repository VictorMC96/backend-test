package com.headhunter.parking.Parking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Objeto de respuesta específico para consultas a endpoints que devuelven información.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaServicioDto extends BaseRespuestaDto {
    /**
     * Objeto con los datos de la respuesta.
     */
    private Object data;

    public RespuestaServicioDto() {

    }
}
