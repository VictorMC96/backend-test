package com.neology.parkingneology.service;

import com.neology.parkingneology.model.Parking;
import com.neology.parkingneology.model.Vehicle;
import com.neology.parkingneology.repository.ParkingRepository;
import com.neology.parkingneology.repository.VehicleRepository;
import com.neology.parkingneology.utils.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceImplTest {
        @Mock
        private ParkingRepository parkingRepository;
        @Mock
        private VehicleRepository vehicleRepository;
        @InjectMocks
        private ParkingService parkingService = new ParkingServiceImpl(parkingRepository, vehicleRepository);



        Vehicle official = new Vehicle() {{
            setPlate("ABC123");
            setType(Type.OFFICIAL);
        }};
        Vehicle resident = new Vehicle() {{
            setPlate("ABC1234");
            setType(Type.RESIDENT);
        }};
        Vehicle visitor = new Vehicle() {{
            setPlate("ABC1235");
            setType(Type.VISITOR);
        }};

        Parking parking = new Parking() {{
            Calendar calendar = Calendar.getInstance();
            setPlate("ABC123");
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            setEntryHour(calendar);
        }};

        Parking parking2 = new Parking() {{
            setPlate("ABC1234");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            setEntryHour(calendar);
            setCost(0.05);
            setMinutes(0);
        }};

        Parking parking3 = new Parking() {{
            setPlate("ABC1235");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            setEntryHour(calendar);
            setCost(0.5);
            setMinutes(0);
        }};


        @Test
        public void when_registerEntry_visitor() {
            Parking parkingS = parkingService.registerEntry("ABC1235");
            assertEquals(parkingS.getPlate(), parking3.getPlate());
            assertEquals(parkingS.getCost(), parking3.getCost());
            assertEquals(parkingS.getMinutes(), parking3.getMinutes());
        }
        @Test
        public void when_registerEntry_oficial() {
            given(vehicleRepository.getVehicleByPlate("ABC123")).willReturn(Optional.of(official));
            given(parkingRepository.getParkingByPlate("ABC123")).willReturn(Optional.of(parking));
            Parking parkingS = parkingService.registerEntry("ABC123");
            assertEquals(parking.getPlate(), parkingS.getPlate());
            assertEquals(parking.getCost(), parkingS.getCost());

        }
        @Test
        public void when_registerEntry_oficial_firsTime() {
            given(vehicleRepository.getVehicleByPlate("ABC123")).willReturn(Optional.of(official));
            given(parkingRepository.getParkingByPlate("ABC123")).willReturn(Optional.empty());
            Parking parkingS = parkingService.registerEntry("ABC123");
            assertEquals(parkingS.getPlate(), parking.getPlate());
            assertEquals(parkingS.getCost(), parking.getCost());

        }

        @Test
        public void when_registerEntry_resident() {
            given(vehicleRepository.getVehicleByPlate("ABC1234")).willReturn(Optional.of(resident));
            given(parkingRepository.getParkingByPlate("ABC1234")).willReturn(Optional.of(parking2));
            Parking parkingS = parkingService.registerEntry("ABC1234");
            assertEquals(parking2.getPlate(), parkingS.getPlate());
            assertEquals(parking2.getCost(), parkingS.getCost());
            assertEquals(parking2.getMinutes(), parkingS.getMinutes());

        }
        @Test
        public void when_registerEntry_resident_firsTime() {
            given(vehicleRepository.getVehicleByPlate("ABC1234")).willReturn(Optional.of(resident));
            given(parkingRepository.getParkingByPlate("ABC1234")).willReturn(Optional.empty());
            Parking parkingS = parkingService.registerEntry("ABC1234");
            assertEquals(parkingS.getPlate(), parking2.getPlate());
            assertEquals(parkingS.getCost(), parking2.getCost());
            assertEquals(parkingS.getMinutes(), parking2.getMinutes());
        }




        @Test()
        public void when_registerExit_exception() {
            given(vehicleRepository.getVehicleByPlate("ABC123")).willReturn(Optional.empty());
            given(parkingRepository.getParkingByPlate("ABC123")).willReturn(Optional.empty());
            try {
                Parking parkingS = parkingService.registerExit("ABC123");
            } catch (RuntimeException e) {
                assertEquals("No existe el vehiculo", e.getMessage());
            }
        }

        @Test()
        public void when_registerExit_oficial() {
            given(vehicleRepository.getVehicleByPlate("ABC123")).willReturn(Optional.of(official));
            given(parkingRepository.getParkingByPlate("ABC123")).willReturn(Optional.of(parking));
            Parking parkingS = parkingService.registerExit("ABC123");
            assertEquals(parkingS.getPlate(), parking.getPlate());
        }
        @Test()
        public void when_registerExit_visitor() {
            given(vehicleRepository.getVehicleByPlate("ABC1235")).willReturn(Optional.of(visitor));
            given(parkingRepository.getParkingByPlate("ABC1235")).willReturn(Optional.of(parking3));
            Parking parkingS = parkingService.registerExit("ABC1235");
            assertEquals(parkingS.getPlate(), parking3.getPlate());
        }

        @Test()
        public void when_registerExit_resident() {
            given(vehicleRepository.getVehicleByPlate("ABC1234")).willReturn(Optional.of(resident));
            given(parkingRepository.getParkingByPlate("ABC1234")).willReturn(Optional.of(parking2));
            Parking parkingS = parkingService.registerExit("ABC1234");
            assertEquals(parkingS.getPlate(), parking2.getPlate());
        }

        @Test
        public void when_registerOfficial() {
            Vehicle vehicle = parkingService.registerOfficial("ABC123");
            assertEquals(vehicle.getPlate(), official.getPlate());
            assertEquals(vehicle.getType(), official.getType());
        }

        @Test
        public void registerResident() {
            Vehicle vehicle = parkingService.registerResident("ABC1234");
            assertEquals(vehicle.getPlate(), resident.getPlate());
            assertEquals(vehicle.getType(), resident.getType());
        }


        @Test
        public void startMonth() {
            List<Vehicle> vehicles = new ArrayList<>(){{add(official);}};
            given(vehicleRepository.getAllVehicleByType(Type.OFFICIAL)).willReturn(vehicles);
            List<Vehicle> vehiclesR = new ArrayList<>(){{add(resident);}};
            given(vehicleRepository.getAllVehicleByType(Type.RESIDENT)).willReturn(vehiclesR);
            given(parkingRepository.getParkingByPlate("ABC1234")).willReturn(Optional.of(parking2));
            given(parkingRepository.getParkingByPlate("ABC123")).willReturn(Optional.of(parking));
            List<Parking> parkingsR = new ArrayList<>(){{add(parking2);}};
            given(parkingRepository.findAll()).willReturn(parkingsR);
            List<Parking> parkings = parkingService.startMonth();
            assertEquals(parkings.get(0).getPlate(), parking2.getPlate());
            assertEquals(parkings.get(0).getCost(), parking2.getCost());
            assertEquals(parkings.get(0).getMinutes(), parking2.getMinutes());
            assertEquals(parkings.size(), 1);
        }

        @Test
        public void pay() {
            List<Vehicle> vehiclesR = new ArrayList<>(){{add(resident);}};
            given(vehicleRepository.getAllVehicleByType(Type.RESIDENT)).willReturn(vehiclesR);
            given(parkingRepository.getParkingByPlate("ABC1234")).willReturn(Optional.of(parking2));
            StringBuilder sb = new StringBuilder();
            sb.append("Núm. placa \tTiempo estacionado (min.) \tCantidad a pagar\n");
            sb.append(parking2.getPlate() + "\t\t\t" + parking2.getMinutes() + "\t\t\t\t\t\t" + parking2.getCost() * parking2.getMinutes() + "\n");
            String response = parkingService.pay("test");
            assertEquals(response, sb.toString());
        }
}
