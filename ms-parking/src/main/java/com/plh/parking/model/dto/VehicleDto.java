package com.plh.parking.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VehicleDto(@NotBlank(message = "may not be null or empty") String plate,
                         @Pattern(regexp = "OFICIAL|RESIDENTE|NO_RESIDENTE", message = "must be 'OFICIAL' or 'RESIDENTE' or 'NO_RESIDENTE'")
                         String type) {
}