package com.daniel_lopez_rivera.backend_test.dto;

import com.daniel_lopez_rivera.backend_test.util.VehiculoTipo;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class VehiculoDTO {

    private String placa;

    private String tipo;

}
