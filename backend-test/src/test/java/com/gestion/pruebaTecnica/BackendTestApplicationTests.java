package com.gestion.pruebaTecnica;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;
import com.gestion.pruebaTecnica.entidades.Vehiculo;
import com.gestion.pruebaTecnica.repositorios.TipoVehiculoRepository;
import com.gestion.pruebaTecnica.repositorios.VehiculoRepository;
import com.gestion.pruebaTecnica.servicio.TipoVehiculoService;
import com.gestion.pruebaTecnica.servicio.TipoVehiculoServiceImpl;
import com.gestion.pruebaTecnica.servicio.VehiculoServiceImpl;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.meta.When;
import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BackendTestApplicationTests {

	@Mock
	private TipoVehiculoServiceImpl tipoVehiculoServiceImpl;

	@Mock
	private VehiculoServiceImpl vehiculoServiceImpl;


	@InjectMocks
	private VehiculoServiceImpl vehiculoService;

	@Test
	public void testSave(){

		TipoVehiculo tipoVehiculo = DataProvider.tipoVehiculoMock();

		this.tipoVehiculoServiceImpl.save(tipoVehiculo);

		verify(this.tipoVehiculoServiceImpl).save(any(TipoVehiculo.class));

	}

	@Test
	public void testFindAll(){

		when(vehiculoServiceImpl.findAll()).thenReturn(DataProvider.vehiculosListMock());
		List<Vehiculo> result = vehiculoServiceImpl.findAll();

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals("ABC1234", result.get(0).getNumeroPlaca());
		assertEquals("OFICIAL", result.get(0).getVehiculo());

	}

}
