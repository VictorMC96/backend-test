package com.plh.parking.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    @JsonProperty("Placa")
    private String plate;

    @JsonProperty("Tiempo estacionado (min.)")
    private String time;

    @JsonProperty("Cantidad a pagar")
    private String fee;

}
