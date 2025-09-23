package mx.ms.parking;
import mx.ms.parking.controller.ParkingC;
import mx.ms.parking.entity.Acceso;
import mx.ms.parking.mutations.Accesos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingC.class)
class MxMsParkingMainApplicationTests {

/*
	@Mock
	private Accesos accesos; // Servicio mockeado

	@InjectMocks
	private ParkingC parkingC; // Controlador bajo prueba

	private Acceso acceso;

	@BeforeEach
	void setUp() {
		acceso = new Acceso();
		acceso.setIdVehiculo(1L);
	}

	@Test
	void testSaveAcceso() {
		// Arrange
		when(accesos.registrarEntrada(any(Acceso.class))).thenReturn(acceso);

		// Act
		Acceso result = parkingC.saveAcceso(acceso);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getIdVehiculo());
		verify(accesos, times(1)).registrarEntrada(any(Acceso.class));
	}

*/



}
