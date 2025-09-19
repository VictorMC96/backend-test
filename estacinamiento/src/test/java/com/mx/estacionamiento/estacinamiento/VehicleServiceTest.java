package com.mx.estacionamiento.estacinamiento;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mx.estacionamiento.estacinamiento.dto.PaymentReportDTO;
import com.mx.estacionamiento.estacinamiento.model.Stay;
import com.mx.estacionamiento.estacinamiento.model.Vehicle;
import com.mx.estacionamiento.estacinamiento.model.VehicleType;
import com.mx.estacionamiento.estacinamiento.repository.StayRepository;
import com.mx.estacionamiento.estacinamiento.repository.VehicleRepository;
import com.mx.estacionamiento.estacinamiento.service.VehicleService;

public class VehicleServiceTest {

    private VehicleRepository vehicleRepository;
    private StayRepository stayRepository;
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        stayRepository = mock(StayRepository.class);
        vehicleService = new VehicleService(vehicleRepository, stayRepository);
    }

    @Test
    void testRegisterExitNoResidenteCalculaPago() {
        // Crear vehículo no residente
        Vehicle v = new Vehicle();
        v.setPlate("XYZ123");
        v.setType(VehicleType.NO_RESIDENTE);

        // Crear estancia abierta
        Stay s = new Stay();
        Calendar entrada = Calendar.getInstance();
        entrada.add(Calendar.MINUTE, -10);
        s.setEntryTime(entrada);
        s.setVehicle(v);

        v.setStays(List.of(s));

        when(vehicleRepository.findById("XYZ123")).thenReturn(Optional.of(v));
        when(stayRepository.save(s)).thenReturn(s);

        double amount = vehicleService.registerExit("XYZ123");

        assertEquals(10 * 0.5, amount);
        assertNotNull(s.getExitTime());
    }

    @Test
    void testRegisterExitResidenteAcumulaMinutos() {
        Vehicle v = new Vehicle();
        v.setPlate("RES001");
        v.setType(VehicleType.RESIDENTE);
        v.setAccumulatedMinutes(0);

        Stay s = new Stay();
        Calendar entrada = Calendar.getInstance();
        entrada.add(Calendar.MINUTE, -15);
        s.setEntryTime(entrada);
        s.setVehicle(v);

        v.setStays(List.of(s));

        when(vehicleRepository.findById("RES001")).thenReturn(Optional.of(v));
        when(stayRepository.save(s)).thenReturn(s);
        when(vehicleRepository.save(v)).thenReturn(v);

        double amount = vehicleService.registerExit("RES001");

        assertEquals(0, amount);
        assertEquals(15, v.getAccumulatedMinutes());
        assertNotNull(s.getExitTime());
    }

    @Test
    void testGenerateResidentReport() {
        Vehicle v1 = new Vehicle();
        v1.setPlate("RES001");
        v1.setType(VehicleType.RESIDENTE);
        v1.setAccumulatedMinutes(100);

        Vehicle v2 = new Vehicle();
        v2.setPlate("RES002");
        v2.setType(VehicleType.RESIDENTE);
        v2.setAccumulatedMinutes(50);

        when(vehicleRepository.findAll()).thenReturn(List.of(v1, v2));

        List<PaymentReportDTO> report = vehicleService.generateResidentReport();

        assertEquals(2, report.size());
        assertEquals(100, report.get(0).getMinutes());
        assertEquals(5.0, report.get(0).getAmount());
        assertEquals(50, report.get(1).getMinutes());
        assertEquals(2.5, report.get(1).getAmount());
    }

}
