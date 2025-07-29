package com.accesoVehicular.neology.Registro;

import com.accesoVehicular.neology.model.Registro;
import com.accesoVehicular.neology.repository.IRegistroRepository;
import com.accesoVehicular.neology.service.AcumuladoService;
import com.accesoVehicular.neology.service.RegistroService;
import com.accesoVehicular.neology.service.TipoVehiculoService;
import com.accesoVehicular.neology.service.VehiculoService;
import com.accesoVehicular.neology.dto.RegistroVehiculo;
import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.model.Vehiculo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock
    private VehiculoService vehiculoService;

    @Mock
    private TipoVehiculoService tipoVehiculoService;

    @Mock
    private IRegistroRepository registroRepository;

    @Mock
    private AcumuladoService acumuladoService;

    @InjectMocks
    private RegistroService registroService;

    @Test
    void errorAlRegistrarEntrada() {
        String placa = "ASDF-JJJII";
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.of(new Registro()));

        Exception exception = assertThrows(Exception.class, () -> registroService.registrarEntrada(placa));
        assertEquals("Placa ya tiene una entrada sin salida registrada", exception.getMessage());
    }

    @Test
    void registraVehiculoYEntrada() throws Exception {
        String placa = "ADRON-1234";
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.empty());
        when(vehiculoService.getByPlaca(placa)).thenReturn(null);
        TipoVehiculo visitante = new TipoVehiculo();
        visitante.setNombre("Visitante");
        when(tipoVehiculoService.findByNombre("Visitante")).thenReturn(visitante);
        when(vehiculoService.registrarVehiculo(any(RegistroVehiculo.class)))
                .thenReturn(new Vehiculo(placa, visitante, true));

        boolean result = registroService.registrarEntrada(placa);

        assertTrue(result);
        verify(registroRepository, times(1)).saveAndFlush(any(Registro.class));
    }

    @Test
    void registraVehiculoYaGuardado() throws Exception {
        String placa = "ADRON-1234";
        Vehiculo vehiculo = new Vehiculo(placa, new TipoVehiculo(), true);
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.empty());
        when(vehiculoService.getByPlaca(placa)).thenReturn(vehiculo);

        boolean result = registroService.registrarEntrada(placa);

        assertTrue(result);
        verify(registroRepository, times(1)).saveAndFlush(any(Registro.class));
    }

    @Test
    void noPuedeRegistrarSalida() {
        String placa = "ADRON-1234";
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> registroService.registrarSalida(placa));
        assertEquals("No hay una entrada registrada para la placa: " + placa, exception.getMessage());
    }

    @Test
    void registraSalidaExitosa() throws Exception {
        String placa = "ADRON-1234";
        TipoVehiculo tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setNombre("Visitante");
        tipoVehiculo.setTarifa(2.0);
        Vehiculo vehiculo = new Vehiculo(placa, tipoVehiculo, true);
        Registro registro = new Registro(vehiculo, Calendar.getInstance());
        registro.setFechaHoraEntrada(Calendar.getInstance());
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.of(registro));

        BigDecimal result = registroService.registrarSalida(placa);

        assertNotNull(result);
        verify(registroRepository, times(1)).saveAndFlush(any(Registro.class));
    }

    @Test
    void registraSalidaExitosaParaResidente() throws Exception {
        String placa = "MHK-655-B";
        TipoVehiculo tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setNombre("RESIDENTE");
        tipoVehiculo.setTarifa(2.0);
        Vehiculo vehiculo = new Vehiculo(placa, tipoVehiculo, true);
        Registro registro = new Registro(vehiculo, Calendar.getInstance());
        registro.setFechaHoraEntrada(Calendar.getInstance());
        when(registroRepository.findByPlacaAndFechaSalidaIsNull(placa)).thenReturn(Optional.of(registro));

        BigDecimal result = registroService.registrarSalida(placa);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result);
        verify(acumuladoService, times(1)).registrarAcumulado(any(Vehiculo.class), anyLong());
        verify(registroRepository, times(1)).saveAndFlush(any(Registro.class));
    }
}