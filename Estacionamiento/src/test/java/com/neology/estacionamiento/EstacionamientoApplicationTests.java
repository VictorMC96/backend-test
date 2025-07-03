package com.neology.estacionamiento;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.neology.estacionamiento.domain.TipoVehiculo;
import com.neology.estacionamiento.service.EstacionamientoService;

@SpringBootTest
class EstacionamientoApplicationTests {

	@Autowired
    EstacionamientoService service;

    @Test
    void testAltaYEntradaSalida() {
        service.altaVehiculo("ABC123", TipoVehiculo.NO_RESIDENTE);
        service.registrarEntrada("ABC123");
        double monto = service.registrarSalida("ABC123");

        assertTrue(monto > 0);
    }
}
