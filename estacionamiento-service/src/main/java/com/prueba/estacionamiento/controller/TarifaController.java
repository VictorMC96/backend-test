package com.prueba.estacionamiento.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.estacionamiento.model.TarifaRequest;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.service.TarifaService;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
	

    private final TarifaService tarifaService = new TarifaService();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @PostMapping("/calcular")
    public ResponseEntity<BigDecimal> calcularTarifa(@RequestBody Map<String, Object> payload) {
        try {
            String placa = (String) payload.get("placa");
            TipoVehiculo tipo = TipoVehiculo.valueOf((String) payload.get("tipo"));
            Calendar entrada = Calendar.getInstance();
            Calendar salida = Calendar.getInstance();

            entrada.setTime(sdf.parse((String) payload.get("entrada")));
            salida.setTime(sdf.parse((String) payload.get("salida")));

            TarifaRequest request = new TarifaRequest();
            request.setPlaca(placa);
            request.setTipo(tipo);
            request.setEntrada(entrada);
            request.setSalida(salida);

            BigDecimal total = tarifaService.calcularTarifa(request);
            return ResponseEntity.ok(total);

        } catch (ParseException parse) {
            return ResponseEntity.badRequest().build();
        }
    }
}
