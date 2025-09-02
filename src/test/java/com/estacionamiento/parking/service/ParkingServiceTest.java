package com.estacionamiento.parking.service;

import com.estacionamiento.parking.domain.*;
import com.estacionamiento.parking.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private VehicleRepository vehicleRepo;

    @Mock
    private ParkingSessionRepository sessionRepo;

    @Mock
    private ResidentAccountRepository residentRepo;

    @InjectMocks
    private ParkingService parkingService;

    private final String plate = "ABC123";

    @BeforeEach
    void setup() {
        Mockito.reset(vehicleRepo, sessionRepo, residentRepo);
    }

    @Test
    void testRegisterEntry_whenNoOpenSession_createsNewSession() {
        when(sessionRepo.findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(plate))
                .thenReturn(Optional.empty());

        when(vehicleRepo.findById(plate)).thenReturn(Optional.empty());

        ArgumentCaptor<ParkingSession> captor = ArgumentCaptor.forClass(ParkingSession.class);
        when(sessionRepo.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        ParkingSession session = parkingService.registerEntry(plate);

        assertNotNull(session);
        assertEquals(plate, session.getPlate());
        assertTrue(session.isOpen());
        assertNull(session.getRegisteredType());

        verify(sessionRepo).save(any(ParkingSession.class));
    }

    @Test
    void testRegisterEntry_whenOpenSessionExists_throwsException() {
        ParkingSession openSession = ParkingSession.builder()
                .plate(plate)
                .open(true)
                .build();

        when(sessionRepo.findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(plate))
                .thenReturn(Optional.of(openSession));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> parkingService.registerEntry(plate));

        assertTrue(ex.getMessage().contains("Ya existe una entrada sin salida"));
    }

    @Test
    void testRegisterExit_whenOpenSessionExists_calculatesAmount() {
        Calendar entryTime = Calendar.getInstance();
        entryTime.add(Calendar.MINUTE, -30);

        ParkingSession openSession = ParkingSession.builder()
                .plate(plate)
                .entryTime(entryTime)
                .open(true)
                .build();

        when(sessionRepo.findFirstByPlateAndOpenTrueOrderByEntryTimeAsc(plate))
                .thenReturn(Optional.of(openSession));

        Vehicle vehicle = Vehicle.builder()
                .plate(plate)
                .type(VehicleType.RESIDENT)
                .build();

        when(vehicleRepo.findById(plate)).thenReturn(Optional.of(vehicle));

        when(sessionRepo.save(any(ParkingSession.class))).thenAnswer(i -> i.getArgument(0));

        ParkingService.ExitResult result = parkingService.registerExit(plate);

        assertEquals(plate, result.plate());
        assertTrue(result.minutes() >= 30);
        assertTrue(result.amountMXN() >= 0);

        verify(sessionRepo).save(any(ParkingSession.class));
    }

    @Test
    void testRegisterOfficial_createsVehicle() {
        Vehicle savedVehicle = Vehicle.builder()
                .plate(plate)
                .type(VehicleType.OFFICIAL)
                .build();

        when(vehicleRepo.save(any(Vehicle.class))).thenReturn(savedVehicle);

        Vehicle result = parkingService.registerOfficial(plate);

        assertEquals(plate, result.getPlate());
        assertEquals(VehicleType.OFFICIAL, result.getType());
    }

    @Test
    void testRegisterResident_createsVehicleAndAccount() {
        Vehicle savedVehicle = Vehicle.builder()
                .plate(plate)
                .type(VehicleType.RESIDENT)
                .build();

        ResidentAccount savedAccount = ResidentAccount.builder()
                .vehicle(savedVehicle)
                .accumulatedMinutes(0)
                .build();

        when(vehicleRepo.save(any(Vehicle.class))).thenReturn(savedVehicle);
        when(residentRepo.save(any(ResidentAccount.class))).thenReturn(savedAccount);

        Vehicle result = parkingService.registerResident(plate);

        assertEquals(plate, result.getPlate());
        assertEquals(VehicleType.RESIDENT, result.getType());
        assertNotNull(result.getResidentAccount());
        assertEquals(0, result.getResidentAccount().getAccumulatedMinutes());
    }
}
