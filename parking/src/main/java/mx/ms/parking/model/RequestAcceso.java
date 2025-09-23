package mx.ms.parking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;

@Data
public class RequestAcceso {
    private Long id;
    private String placa;
    private Boolean acceso;//0 si es entrada y 1 si es salida
}
