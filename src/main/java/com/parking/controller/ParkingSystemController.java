package com.parking.controller;

import com.parking.dto.Car;
import com.parking.repository.ParkingSystemRepository;
import com.parking.service.ParkingSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

@RestController
@RequestMapping("/parking")
public class ParkingSystemController {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSystemController.class);
    private static final String OFFICIAL = "OFFICIAL";
    private static final String RESIDENT = "RESIDENT";
    private static final String NON_RESIDENT = "NON_RESIDENT";
    private static final Double RESIDENT_FEE = 0.05;
    private static final Double NON_RESIDENT_FEE = 0.5;

    private final ParkingSystemRepository parkingSystemRepository;
    private final ParkingSystemService parkingSystemService;

    public ParkingSystemController(ParkingSystemRepository parkingSystemRepository, ParkingSystemService parkingSystemService) {
        this.parkingSystemRepository = parkingSystemRepository;
        this.parkingSystemService = parkingSystemService;
    }

    /* EJERCICIO A. Caso de uso "Registra entrada"
    El empleado elige la opción "registrar entrada" e introduce el número de placa del coche que entra.
    La aplicación apunta la hora de entrada del vehículo. */
    @PostMapping("/addCar/{licensePlate}")
    public ResponseEntity<Void> addCar(@PathVariable String licensePlate, UriComponentsBuilder ucb) {
        return parkingSystemService.addCar(licensePlate, ucb);
    }

    /* EJERCICIO B. Caso de uso "Registra salida"
    El empleado elige la opción "registrar salida" e introduce el número de placa del coche que sale.
    La aplicación realiza las acciones correspondientes al tipo de vehículo:
    Oficial: asocia la estancia (hora de entrada y hora de salida) con el vehículo
    Residente: suma la duración de la estancia al tiempo total acumulado
    No residente: obtiene el importe a pagar*/
    @PostMapping("/removeCar/{licensePlate}")
    public ResponseEntity<Void> removeCar(@PathVariable String licensePlate) {
        return parkingSystemService.removeCar(licensePlate);
    }

    /* EJERCICIO C. Caso de uso "Da de alta vehículo oficial"
        El empleado elige la opción "dar de alta vehículo oficial" e introduce su número de placa.
        La aplicación añade el vehículo a la lista de vehículos oficiales*/
    @PostMapping("/addOfficialCar/{licensePlate}")
    public ResponseEntity<Void> addOfficialCar(@PathVariable String licensePlate) {
        return parkingSystemService.addOfficialCar(licensePlate);
    }

    /* EJERCICIO D. Caso de uso "Da de alta vehículo residente"
        El empleado elige la opción "dar de alta vehículo residente" e introduce su número de placa.
        La aplicación añade el vehículo a la lista de vehículos residentes*/
    @PostMapping("/addResidentCar/{licensePlate}")
    public ResponseEntity<Void> addResidentCar(@PathVariable String licensePlate) {
        return parkingSystemService.addResidentCar(licensePlate);
    }

    /* EJERCICIO E. Caso de uso "Comienza mes"
        El empleado elige la opción "comienza mes".
        La aplicación elimina las estancias registradas en los coches oficiales y pone a cero el tiempo estacionado por los vehículos de residentes.*/
    @PostMapping("/startMonth")
    public ResponseEntity<Void> startMonth() {
        return parkingSystemService.startMonth();
    }

    /* EJERCICIO F. Caso de uso "Pagos de residentes"
        El empleado elige la opción "genera informe de pagos de residentes" e introduce el nombre del archivo en el que quiere generar el informe.
        La aplicación genera un archivo que detalla el tiempo estacionado y el dinero a pagar por cada uno de los vehículos de residentes. El formato del archivo será el mostrado a continuación:
        Núm. placa 	Tiempo estacionado (min.) 	Cantidad a pagar
        S1234A 	    20134 				        1006.70
        4567ABC	    4896				        244.80*/
    @GetMapping("/residentPayments")
    public ResponseEntity<String> generateResidentPaymentsReport() {
        return parkingSystemService.generateResidentPaymentsReport();
    }
}

