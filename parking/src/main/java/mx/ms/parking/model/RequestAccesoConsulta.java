package mx.ms.parking.model;

import lombok.Data;

import java.util.Calendar;

@Data
public class RequestAccesoConsulta {
    private Long idVehiculo;
    private Calendar fhEntrada;
}
