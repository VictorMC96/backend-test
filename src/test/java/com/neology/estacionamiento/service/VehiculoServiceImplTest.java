package com.neology.estacionamiento.service;

import com.neology.estacionamiento.model.Estancia;
import com.neology.estacionamiento.model.TipoVehiculo;
import com.neology.estacionamiento.model.Vehiculo;
import com.neology.estacionamiento.repository.EstanciaRepository;
import com.neology.estacionamiento.repository.VehiculoRepository;
import com.neology.estacionamiento.util.Constantes;
import com.neology.estacionamiento.util.FechaUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

class VehiculoServiceImplTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private EstanciaRepository estanciaRepository;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarEntrada_Success() {
        String placa = "ABC123";
        when(vehiculoRepository.existsById(placa)).thenReturn(true);
        when(estanciaRepository.findByPlacaAndHoraSalidaIsNull(placa)).thenReturn(Optional.empty());
        when(estanciaRepository.save(any(Estancia.class))).thenAnswer(inv -> inv.getArgument(0));

        Estancia result = vehiculoService.registrarEntrada(placa);

        assertNotNull(result);
        assertEquals(placa, result.getPlaca());
    }

    @Test
    void testRegistrarEntrada_NoRegistrado() {
        String placa = "XYZ999";
        when(vehiculoRepository.existsById(placa)).thenReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                vehiculoService.registrarEntrada(placa));
        assertEquals(Constantes.VEHICULO_NO_REGISTRADO, ex.getMessage());
    }

    @Test
    void testRegistrarSalida_NoResidente() {
        String placa = "NR001";
        Estancia estancia = new Estancia(placa, LocalDateTime.now().minusMinutes(20));
        Vehiculo vehiculo = new Vehiculo(placa, TipoVehiculo.NO_RESIDENTE);

        when(estanciaRepository.findByPlacaAndHoraSalidaIsNull(placa)).thenReturn(Optional.of(estancia));
        when(vehiculoRepository.findById(placa)).thenReturn(Optional.of(vehiculo));

        String resultado = vehiculoService.registrarSalida(placa);

        assertTrue(resultado.contains("Total a pagar"));
    }

    @Test
    void testRegistrarVehiculo_YaExiste() {
        Vehiculo vehiculo = new Vehiculo("DUP123", TipoVehiculo.RESIDENTE);
        when(vehiculoRepository.existsById("DUP123")).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                vehiculoService.registrarVehiculo(vehiculo, TipoVehiculo.RESIDENTE));
        assertEquals(Constantes.VEHICULO_YA_REGISTRADO, ex.getMessage());
    }

    @Test
    void testReiniciarMes() {
        Vehiculo v1 = new Vehiculo("RES1", TipoVehiculo.RESIDENTE);
        v1.setMinutosAcumulados(150);

        when(vehiculoRepository.findAll()).thenReturn(List.of(v1));

        vehiculoService.reiniciarMes();

        verify(estanciaRepository, times(1)).deleteAll();
        verify(vehiculoRepository, times(1)).saveAll(any());
    }

    @Test
    void testGenerarReporteResidentes() {
        Vehiculo residente = new Vehiculo("RES123", TipoVehiculo.RESIDENTE);
        residente.setMinutosAcumulados(100);

        when(vehiculoRepository.findAll()).thenReturn(List.of(residente));

        List<Map<String, Object>> reporte = vehiculoService.generarReporteResidentes();

        assertEquals(1, reporte.size());
        assertEquals("RES123", reporte.get(0).get("placa"));
        assertEquals(100, reporte.get(0).get("minutos"));
    }
}
