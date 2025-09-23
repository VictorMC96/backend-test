package mx.ms.parking.model;

import lombok.Data;

@Data
public class RequestVehiculo {
    private String placas;
    private Long tipoVehiculo;
}
