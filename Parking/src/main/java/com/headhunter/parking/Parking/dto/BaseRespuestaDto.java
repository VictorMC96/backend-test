package com.headhunter.parking.Parking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Objeto para las respuestas base de los endpoints, puede utilizarse para respuestas de error o respuestas que
 * no devuelven datos.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseRespuestaDto {

    /**
     * Código de estado de la respuesta HTTP.
     */
    private Integer statusCode;

    /**
     * Mensaje escrito de la respuesta, si es el caso.
     */
    private String message;

    /**
     * Fecha de la respuesta.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date timestamp = new Date();
}
