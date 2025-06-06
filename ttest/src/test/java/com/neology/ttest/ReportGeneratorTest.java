package com.neology.ttest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.neology.parkinglot.dao.ParkingLotDao;
import com.neology.parkinglot.entity.vehicles.Vehicle;
import com.neology.parkinglot.util.ReportGenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ReportGeneratorTest {

    private final String testFileName = "test_residentes.txt";

    @BeforeEach
    void setup() {
        // Clean test file before each test
        new File(testFileName).delete();
    }

    @AfterEach
    void cleanup() {
        new File(testFileName).delete();
    }

    @Test
    void testGenerarInformeResidentes() throws Exception {
        Vehicle vehicle = mock(Vehicle.class);
        when(vehicle.getVpNumber()).thenReturn("ABC123");
        when(vehicle.getTotalTime()).thenReturn(100);
        ParkingLotDao mockedDao = mock(ParkingLotDao.class);
        when(mockedDao.getResidentCars()).thenReturn(List.of(vehicle));
        try (MockedStatic<ParkingLotDao> mockedStatic = mockStatic(ParkingLotDao.class)) {
            mockedStatic.when(ParkingLotDao::new).thenReturn(mockedDao);

            ReportGenerator.generarInformeResidentes(testFileName);
            File file = new File(testFileName);
            assertTrue(file.exists());

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String header = reader.readLine();
                String data = reader.readLine();

                assertTrue(header.contains("Núm. placa"));
                assertTrue(data.contains("ABC123"));
                assertTrue(data.contains("100"));
                assertTrue(data.contains("5.00")); // 100 * 0.05
            }
        }
    }
}
