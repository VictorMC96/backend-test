package com.headhunter.parking.Parking.utils;

import com.headhunter.parking.Parking.dto.RespuestaServicioDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RespuestaUtils {

    // Private constructor to hide the implicit public one
    private RespuestaUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Crea un objeto RespuestaServicioDto con la respuesta proporcionada.
     *
     * @param respuesta Objeto de respuesta.
     * @return RespuestaServicioDto creado.
     */
    public static RespuestaServicioDto procesaRespuesta(Object respuesta, Integer codigo) {
        return RespuestaServicioDto.builder()
                .data(respuesta)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .statusCode(codigo)
                .build();
    }
}
