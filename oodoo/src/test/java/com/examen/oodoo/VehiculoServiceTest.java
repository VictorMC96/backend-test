package com.examen.oodoo;

import com.examen.oodoo.entity.Estancia;
import com.examen.oodoo.entity.Vehiculo;
import com.examen.oodoo.enums.TipoVehiculo;
import com.examen.oodoo.repository.EstanciaRepository;
import com.examen.oodoo.repository.VehiculoRepository;
import com.examen.oodoo.service.VehiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VehiculoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private EstanciaRepository estanciaRepository;

    @InjectMocks
    private VehiculoService vehiculoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarEntradaVehiculoExistenteSinEstancia() {
        Vehiculo vehiculoExistente = new Vehiculo("ABC123", TipoVehiculo.OFICIAL);
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(vehiculoExistente);
        String resultado = vehiculoService.registrarEntrada("ABC123");
        assertEquals("Registro de entrada exitoso para el vehículo con placa ABC123", resultado);
        verify(vehiculoRepository, times(1)).save(eq(vehiculoExistente));
    }

    @Test
    public void testRegistrarEntradaVehiculoExistenteConEstanciaActiva() {
        Vehiculo vehiculoExistente = new Vehiculo("ABC123", TipoVehiculo.OFICIAL);
        Estancia estanciaExistente = new Estancia();
        estanciaExistente.setHoraSalida(null); // Simula una estancia activa sin hora de salida
        vehiculoExistente.getEstancias().add(estanciaExistente);
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(vehiculoExistente);
        String resultado = vehiculoService.registrarEntrada("ABC123");
        assertEquals("Ya existe una estancia activa para el vehículo con placa ABC123", resultado);
        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    public void testRegistrarEntradaVehiculoNoExistente() {
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(null);
        String resultado = vehiculoService.registrarEntrada("XYZ789");
        assertEquals("El vehículo con placa XYZ789 no está registrado. No se puede registrar la entrada.", resultado);
        verify(vehiculoRepository, never()).save(any());
    }

    @Test
    void testRegistrarSalida() {
        String placa = "ABC123";
        Vehiculo vehiculoExistente = new Vehiculo();
        List<Estancia> estancias = new ArrayList<>();
        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculoExistente);
        estancias.add(estancia);
        vehiculoExistente.setEstancias(estancias);

        when(vehiculoRepository.findByPlaca(placa)).thenReturn(vehiculoExistente);
        vehiculoService.registrarSalida(placa);
        verify(estanciaRepository, times(1)).save(any());
    }

    @Test
    void testRegistrarPlacaOficial() {
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(null);
        String resultado = vehiculoService.darDeAltaVehiculoOficial("ABC123av");
        assertEquals("Registro exitoso para el vehículo con placa ABC123av", resultado);
        verify(vehiculoRepository, times(1)).save(argThat(vehiculo -> "ABC123av".equals(vehiculo.getPlaca())));
    }

    @Test
    void testRegistrarPlacaOficiala() {
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(null);
        String resultado = vehiculoService.darDeAltaVehiculoResidente("ABC123av");
        assertEquals("Registro exitoso para el vehículo con placa ABC123av", resultado);
        verify(vehiculoRepository, times(1)).save(argThat(vehiculo -> "ABC123av".equals(vehiculo.getPlaca())));
    }
}
