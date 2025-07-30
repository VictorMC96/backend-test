package com.accesoVehicular.neology.Vehiculo;

import com.accesoVehicular.neology.dto.RegistroVehiculo;
import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.model.Vehiculo;
import com.accesoVehicular.neology.repository.IVehiculoRepository;
import com.accesoVehicular.neology.service.TipoVehiculoService;
import com.accesoVehicular.neology.service.VehiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private IVehiculoRepository vehicleRepository;

    @Mock
    private TipoVehiculoService tipoVehiculoService;

    @InjectMocks
    private VehiculoService vehiculoService;

    @Test
    void registroSinCampos() {
        Exception exception = assertThrows(Exception.class, () -> vehiculoService.registrarVehiculo(null));
        assertEquals("Campos faltantes en la solicitud de registro de vehiculo", exception.getMessage());
    }


    @Test
    void vehiculoYaExiste() {
        RegistroVehiculo request = new RegistroVehiculo();
        request.setPlaca("ABC123");

        when(vehicleRepository.findByPlaca("ABC123")).thenReturn(Optional.of(new Vehiculo()));

        Exception exception = assertThrows(Exception.class, () -> vehiculoService.registrarVehiculo(request));
        assertEquals("Vehiculo ya registrado con la placa: ABC123", exception.getMessage());
    }

    @Test
    void vehiculoGuardaConExito() throws Exception {
        RegistroVehiculo request = new RegistroVehiculo();
        request.setPlaca("R1-A1");
        request.setTipoVehiculo(new TipoVehiculo());

        Vehiculo vehiculo = new Vehiculo("R1-A1", new TipoVehiculo(), true);
        when(vehicleRepository.saveAndFlush(any(Vehiculo.class))).thenReturn(vehiculo);

        Vehiculo result = vehiculoService.registrarVehiculo(request);

        assertNotNull(result);
        assertEquals("R1-A1", result.getPlaca());
    }

    @Test
    void placaNoExiste() {
        Vehiculo vehiculo = new Vehiculo("V1-AAA-BBB", new TipoVehiculo(), true);
        when(vehicleRepository.findByPlaca("V1-AAA-BBB")).thenReturn(Optional.of(vehiculo));

        Vehiculo result = vehiculoService.getByPlaca("V1-AAA-BBB");

        assertNotNull(result);
        assertEquals("V1-AAA-BBB", result.getPlaca());
    }

    @Test
    void findAllExitoso() {
        List<Vehiculo> vehiculos = List.of(
                new Vehiculo("CHIEF-117", new TipoVehiculo(), true),
                new Vehiculo("LINDA-118", new TipoVehiculo(), true)
        );
        when(vehicleRepository.findAll()).thenReturn(vehiculos);

        List<Vehiculo> result = vehiculoService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getByTipoVehiculoExitoso() {
        List<Vehiculo> vehiculos = List.of(
                new Vehiculo("GI-GOE", new TipoVehiculo(), true)
        );
        when(vehicleRepository.findByTipoVehiculoNombre("OFICIAL")).thenReturn(vehiculos);

        List<Vehiculo> result = vehiculoService.getByTipoVehiculoNombre("OFICIAL");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("GI-GOE", result.get(0).getPlaca());
    }
}