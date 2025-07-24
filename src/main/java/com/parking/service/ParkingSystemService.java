package com.parking.service;

import com.parking.dto.Car;
import com.parking.repository.ParkingSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

@Service
public class ParkingSystemService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSystemService.class);

    private static final String OFFICIAL = "OFFICIAL";
    private static final String RESIDENT = "RESIDENT";
    private static final String NON_RESIDENT = "NON_RESIDENT";
    private static final Double RESIDENT_FEE = 0.05;
    private static final Double NON_RESIDENT_FEE = 0.5;

    private final ParkingSystemRepository parkingSystemRepository;

    public ParkingSystemService(ParkingSystemRepository parkingSystemRepository) {
        this.parkingSystemRepository = parkingSystemRepository;
    }

    public ResponseEntity<Void> addCar(String licensePlate, UriComponentsBuilder ucb) {
        Car existingCar = null;
        if (parkingSystemRepository.existsByPlate(licensePlate)) {
            existingCar = parkingSystemRepository.findByPlate(licensePlate);
            if (parkingSystemRepository.findInsideParkingLotByPlate(existingCar.plate())) {
                logger.warn("Car with license plate {} already is in the parking lot, take the vehicle out of the parking lot", licensePlate);
                return ResponseEntity.badRequest().build();
            }
        }

        Calendar currentTime = Calendar.getInstance();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
        Timestamp startTimestamp = Timestamp.valueOf(localDateTime);

        if (existingCar != null) {
            existingCar = new Car(existingCar.plate(), existingCar.type(), true, existingCar.stayingMinutes(), startTimestamp, null);
            parkingSystemRepository.save(existingCar);
            logger.info("Updated existing car with plate {}", licensePlate);
            return ResponseEntity.ok().build();
        }
        Car car = new Car(licensePlate, null, true, 0, startTimestamp, null);
        parkingSystemRepository.save(car);

        URI locationNewCarRecord = ucb.path("cars/{licensePlate}")
                .buildAndExpand(car.plate())
                .toUri();

        return ResponseEntity.created(locationNewCarRecord).build();
    }

    public ResponseEntity<Void> removeCar(String licensePlate) {
        Car car = parkingSystemRepository.findByPlate(licensePlate);
        if (car == null) {
            logger.warn("Car with license plate {} not found", licensePlate);
            return ResponseEntity.notFound().build();
        }
        if (car.type() == null || car.startDateTime() == null) {
            logger.warn("Car has nullables for plate {}", car.plate());
            return ResponseEntity.badRequest().build();
        }

        // Calculate the end time
        Calendar currentTime = Calendar.getInstance();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(currentTime.toInstant(), ZoneId.systemDefault());
        Timestamp endTimestamp = Timestamp.valueOf(localDateTime);

        // Calculate the duration of the staying in minutes
        LocalDateTime start = car.startDateTime().toLocalDateTime();
        LocalDateTime end = endTimestamp.toLocalDateTime();
        long minutes = Duration.between(start, end).toMinutes();
        if (minutes <= 0) {
            logger.warn("End time is before start time for car with plate {}", car.plate());
            return ResponseEntity.badRequest().build();
        }
        int totalMinutes = car.stayingMinutes() + (int) minutes;//update the total minutes of staying
        double nonResidentFee = 0.0;
        //Official, Resident, no calculate fee, only update minutes and times
        if (car.type().equals(NON_RESIDENT)) {// Non-resident car: calculate the amount to pay
            nonResidentFee = totalMinutes * NON_RESIDENT_FEE; // Example rate of 0.5 per minute
            logger.info("Non-resident car with plate {} has a fee of {}", car.plate(), nonResidentFee);
        } else {
            logger.warn("Unknown car type for plate {}", car.plate());
            return ResponseEntity.badRequest().build();
        }
        // Update the car's end time
        Car updatedCar = new Car(car.plate(), car.type(), false, totalMinutes, car.startDateTime(), endTimestamp);
        parkingSystemRepository.save(updatedCar);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> addOfficialCar(String licensePlate) {
        if (parkingSystemRepository.existsByPlate(licensePlate)) {
            logger.warn("Official car with plate {} already exists", licensePlate);
            return ResponseEntity.badRequest().build();
        }
        Car officialCar = new Car(licensePlate, OFFICIAL, false, 0, null, null);
        parkingSystemRepository.save(officialCar);
        logger.info("Added official car with plate {}", licensePlate);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> addResidentCar(String licensePlate) {
        if (parkingSystemRepository.existsByPlate(licensePlate)) {
            logger.warn("Resident car with plate {} already exists", licensePlate);
            return ResponseEntity.badRequest().build();
        }
        Car residentCar = new Car(licensePlate, RESIDENT, false, 0, null, null);
        parkingSystemRepository.save(residentCar);
        logger.info("Added resident car with plate {}", licensePlate);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> startMonth() {
        Iterable<Car> cars = parkingSystemRepository.findAll();
        if (!cars.iterator().hasNext()) {
            logger.warn("No cars found");
            return ResponseEntity.notFound().build();
        }
        for (Car car : cars) {
            if (car.type() == null) {
                logger.warn("Car with plate {} has null type, skipping", car.plate());
                continue;
            }
            if (car.type().equals(OFFICIAL)) {
                // Remove official car records
                parkingSystemRepository.delete(car);
                logger.info("Removed official car with plate {}", car.plate());
            } else if (car.type().equals(RESIDENT)) {
                // Reset resident car staying minutes to zero
                Car updatedResidentCar = new Car(car.plate(), RESIDENT, false, 0, null, null);
                parkingSystemRepository.save(updatedResidentCar);
                logger.info("Reset resident car with plate {}", car.plate());
            }
        }
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<String> generateResidentPaymentsReport() {
        Iterable<Car> cars = parkingSystemRepository.findAll();
        if (!cars.iterator().hasNext()) {
            logger.warn("No cars found in the parking system");
            return ResponseEntity.notFound().build();
        }
        StringBuilder report = new StringBuilder();
        report.append("Plate \tStaying time (min)\tPayment\n");

        for (Car car : cars) {
            if (car.type() != null && car.type().equals(RESIDENT)) {
                double amountToPay = car.stayingMinutes() * RESIDENT_FEE;
                report.append(String.format("%s\t%d\t%.2f\n", car.plate(), car.stayingMinutes(), amountToPay));
            }
        }

        // for simplicity, we return it as a string
        logger.info("Generated resident payments report");
        return ResponseEntity.ok(report.toString());
    }

}
