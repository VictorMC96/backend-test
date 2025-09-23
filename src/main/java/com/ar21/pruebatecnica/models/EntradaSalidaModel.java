package com.ar21.pruebatecnica.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntradaSalidaModel {

    private String placa;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHoraEntrada;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHoraSalida;

    private String tipoMovimiento;

    private String estanciaTotalAcumulado;

    private String importeAPagar;



    public String calcularTiempo(LocalDateTime inicio, LocalDateTime salida) {
        Duration duracion = Duration.between(inicio, salida);

        return String.format("%02d:%02d:%02d",
                duracion.toHours(),
                duracion.toMinutes() % 60,
                duracion.getSeconds() % 60);
    }

}

