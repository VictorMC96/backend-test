package com.parking;

import com.parking.controller.ParkingSystemController;
import com.parking.dto.Car;
import com.parking.repository.ParkingSystemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkingSystemControllerTest {

    @Mock
    private ParkingSystemRepository parkingSystemRepository;

    @InjectMocks
    private ParkingSystemController controller;

    private UriComponentsBuilder ucb;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ucb = UriComponentsBuilder.newInstance();
    }

    // tests for addCar() endpoint
    @Test
    void addCarAddANewCar() {
        String plate = "NEW123";
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(false);

        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(parkingSystemRepository).save(any(Car.class));
    }

    @Test
    void addCarUpdatesAnExistingCar() {
        String plate = "EXIST123";
        Car existingCar = new Car(plate, "RESIDENT", false, 10, new Timestamp(System.currentTimeMillis()), null);
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(true);
        when(parkingSystemRepository.findByPlate(plate)).thenReturn(existingCar);
        when(parkingSystemRepository.findInsideParkingLotByPlate(plate)).thenReturn(false);

        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemRepository).save(any(Car.class));
    }

    @Test
    void addCarACarIsInTheParkingLot() {
        String plate = "INSIDE123";
        Car existingCar = new Car(plate, "RESIDENT", true, 10, new Timestamp(System.currentTimeMillis()), null);
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(true);
        when(parkingSystemRepository.findByPlate(plate)).thenReturn(existingCar);
        when(parkingSystemRepository.findInsideParkingLotByPlate(plate)).thenReturn(true);

        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemRepository, never()).save(any());
    }

    // tests for removeCar() endpoint
    @Test
    void removeCarCarNotFound() {
        String plate = "NOTFOUND";
        when(parkingSystemRepository.findByPlate(plate)).thenReturn(null);

        ResponseEntity<Void> response = controller.removeCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(parkingSystemRepository, never()).save(any());
    }

    @Test
    void removeCarCarWithNullValues() {
        String plate = "NULLPLATE";
        Car car = new Car(plate, null, true, 0, null, null);
        when(parkingSystemRepository.findByPlate(plate)).thenReturn(car);

        ResponseEntity<Void> response = controller.removeCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemRepository, never()).save(any());
    }

    // tests for addOfficialCar() endpoint
    @Test
    void addOfficialCarNewOfficialCar() {
        String plate = "OFF123";
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(false);

        ResponseEntity<Void> response = controller.addOfficialCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemRepository).save(any(Car.class));
    }

    @Test
    void addOfficialCarAlreadyExists() {
        String plate = "OFF123";
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(true);

        ResponseEntity<Void> response = controller.addOfficialCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemRepository, never()).save(any());
    }

    // tests for addResidentCar() endpoint
    @Test
    void addResidentCarNewResidentCar() {
        String plate = "OFF123";
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(false);

        ResponseEntity<Void> response = controller.addResidentCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemRepository).save(any(Car.class));
    }

    @Test
    void addResidentCarAlreadyExists() {
        String plate = "OFF123";
        when(parkingSystemRepository.existsByPlate(plate)).thenReturn(true);

        ResponseEntity<Void> response = controller.addResidentCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemRepository, never()).save(any());
    }

    // tests for startMonth() endpoint
    @Test
    void startMonthNoCars() {
        Iterable<Car> emptyList = Collections.emptyList();
        when(parkingSystemRepository.findAll()).thenReturn(emptyList);

        ResponseEntity<Void> response = controller.startMonth();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(parkingSystemRepository, never()).delete(any());
        verify(parkingSystemRepository, never()).save(any());
    }

    @Test
    void startMonthVerifyOperations() {
        Car official = new Car("OFF1", "OFFICIAL", false, 100, null, null);
        Car resident = new Car("RES1", "RESIDENT", false, 200, null, null);
        Iterable<Car> cars = Arrays.asList(official, resident);
        when(parkingSystemRepository.findAll()).thenReturn(cars);

        ResponseEntity<Void> response = controller.startMonth();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemRepository).delete(official);
        verify(parkingSystemRepository).save(argThat(car ->
                car.plate().equals("RES1") &&
                        car.type().equals("RESIDENT") &&
                        car.stayingMinutes() == 0
        ));
    }

    // tests for generateResidentPaymentsReport() endpoint
    @Test
    void generateResidentPaymentsReportNoCars() {
        when(parkingSystemRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = controller.generateResidentPaymentsReport();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void generateResidentPaymentsReportReturnsReport() {
        Car resident1 = new Car("RES1", "RESIDENT", false, 120, null, null);
        Car resident2 = new Car("RES2", "RESIDENT", false, 60, null, null);
        Car official = new Car("OFF1", "OFFICIAL", false, 100, null, null);
        Iterable<Car> cars = Arrays.asList(resident1, resident2, official);
        when(parkingSystemRepository.findAll()).thenReturn(cars);

        ResponseEntity<String> response = controller.generateResidentPaymentsReport();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = response.getBody();
        assertThat(body).contains("Plate \tStaying time (min)\tPayment");
        assertThat(body).contains("RES1\t120\t6.00");
        assertThat(body).contains("RES2\t60\t3.00");
        assertThat(body).doesNotContain("OFF1");
    }
}