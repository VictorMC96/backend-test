package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.NoResidente;
import com.neology.estacionamiento.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private ParkingService parkingService;

    @Test
    void testRegistroEntradaSalidaNoResidente() {
        // Simular hora de entrada hace 10 minutos
        Calendar entrada = Calendar.getInstance();
        entrada.add(Calendar.MINUTE, -10);

        NoResidente vehiculo = new NoResidente();
        vehiculo.setPlaca("ABC123");
        vehiculo.setHoraEntrada(entrada);

        when(vehiculoRepository.findByPlacaAndHoraSalidaIsNull("ABC123"))
                .thenReturn(Optional.of(vehiculo));

        double monto = parkingService.registrarSalida("ABC123");

        assertTrue(monto > 0, "El monto debe ser mayor que cero");
    }
}
