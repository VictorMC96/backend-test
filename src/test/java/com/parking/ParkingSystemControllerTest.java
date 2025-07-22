package com.parking;

import com.parking.controller.ParkingSystemController;

import com.parking.service.ParkingSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkingSystemControllerTest {

    @Mock
    private ParkingSystemService parkingSystemService;

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
        when(parkingSystemService.addCar(eq(plate), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.created(ucb.path("cars/{licensePlate}")
                                .buildAndExpand(plate)
                                .toUri())
                        .build());
        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(parkingSystemService).addCar(eq(plate), any(UriComponentsBuilder.class));
    }

    @Test
    void addCarUpdatesAnExistingCar() {
        String plate = "EXIST123";
        when(parkingSystemService.addCar(eq(plate), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemService).addCar(eq(plate), any(UriComponentsBuilder.class));
    }

    @Test
    void addCarACarIsInTheParkingLot() {
        String plate = "INSIDE123";
        when(parkingSystemService.addCar(eq(plate), any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<Void> response = controller.addCar(plate, ucb);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemService).addCar(eq(plate), any(UriComponentsBuilder.class));
    }

    // tests for removeCar() endpoint
    @Test
    void removeCarCarNotFound() {
        String plate = "NOTFOUND";
        when(parkingSystemService.removeCar(plate))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<Void> response = controller.removeCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(parkingSystemService).removeCar(plate);
    }

    @Test
    void removeCarCarWithNullValues() {
        String plate = "NULLPLATE";
        when(parkingSystemService.removeCar(plate))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<Void> response = controller.removeCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemService).removeCar(plate);
    }

    // tests for addOfficialCar() endpoint
    @Test
    void addOfficialCarNewOfficialCar() {
        String plate = "OFF123";
        when(parkingSystemService.addOfficialCar(plate))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = controller.addOfficialCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemService).addOfficialCar(plate);
    }

    @Test
    void addOfficialCarAlreadyExists() {
        String plate = "OFF123";
        when(parkingSystemService.addOfficialCar(plate))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<Void> response = controller.addOfficialCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemService).addOfficialCar(plate);
    }

    // tests for addResidentCar() endpoint
    @Test
    void addResidentCarNewResidentCar() {
        String plate = "OFF123";
        when(parkingSystemService.addResidentCar(plate))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = controller.addResidentCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemService).addResidentCar(plate);
    }

    @Test
    void addResidentCarAlreadyExists() {
        String plate = "OFF123";
        when(parkingSystemService.addResidentCar(plate))
                .thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity<Void> response = controller.addResidentCar(plate);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(parkingSystemService).addResidentCar(plate);
    }

    // tests for startMonth() endpoint
    @Test
    void startMonthNoCars() {
        when(parkingSystemService.startMonth())
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<Void> response = controller.startMonth();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(parkingSystemService).startMonth();
    }

    @Test
    void startMonthVerifyOperations() {
        when(parkingSystemService.startMonth())
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = controller.startMonth();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(parkingSystemService).startMonth();
    }


    // tests for generateResidentPaymentsReport() endpoint
    @Test
    void generateResidentPaymentsReportNoCars() {
        when(parkingSystemService.generateResidentPaymentsReport())
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<String> response = controller.generateResidentPaymentsReport();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(parkingSystemService).generateResidentPaymentsReport();
    }

    @Test
    void generateResidentPaymentsReportOk() {
        String report = "Plate \tStaying time (min)\tPayment\nRES1\t100\t5.00\n";
        when(parkingSystemService.generateResidentPaymentsReport())
                .thenReturn(ResponseEntity.ok(report));

        ResponseEntity<String> response = controller.generateResidentPaymentsReport();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(report);
        verify(parkingSystemService).generateResidentPaymentsReport();
    }
}