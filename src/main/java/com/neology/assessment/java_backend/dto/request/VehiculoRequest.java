package com.neology.assessment.java_backend.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehiculoRequest  implements Serializable {
    private String placa;
}
