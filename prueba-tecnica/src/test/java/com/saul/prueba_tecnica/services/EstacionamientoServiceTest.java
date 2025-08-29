package com.saul.prueba_tecnica.services;

import com.saul.prueba_tecnica.entities.*;
import com.saul.prueba_tecnica.repositories.EstanciaRepository;
import com.saul.prueba_tecnica.repositories.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class EstacionamientoServiceTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private EstanciaRepository estanciaRepository;

    @InjectMocks
    private EstacionamientoService estacionamientoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarVehiculoOficial() {
        estacionamientoService.registrarVehiculoOficial("ABC123");

        ArgumentCaptor<Vehiculo> captor = ArgumentCaptor.forClass(Vehiculo.class);
        verify(vehiculoRepository).save(captor.capture());

        Vehiculo vehiculo = captor.getValue();
        assertEquals("ABC123", vehiculo.getPlaca());
        assertEquals(TipoVehiculo.OFICIAL, vehiculo.getTipo());
    }

    @Test
    void testRegistrarEntrada() {
        Vehiculo v = new Vehiculo();
        v.setPlaca("XYZ789");
        v.setTipo(TipoVehiculo.NO_RESIDENTE);

        when(vehiculoRepository.findById("XYZ789")).thenReturn(Optional.of(v));

        estacionamientoService.registrarEntrada("XYZ789");

        verify(estanciaRepository).save(any(Estancia.class));
    }

    @Test
    void testRegistrarSalidaNoResidente() {
        Vehiculo v = new Vehiculo();
        v.setPlaca("ABC123");
        v.setTipo(TipoVehiculo.NO_RESIDENTE);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -60);

        Estancia estancia = new Estancia();
        estancia.setEntrada(cal.getTime());
        estancia.setVehiculo(v);

        when(vehiculoRepository.findById("ABC123")).thenReturn(Optional.of(v));
        when(estanciaRepository.findTopByVehiculoOrderByEntradaDesc(v)).thenReturn(estancia);

        double monto = estacionamientoService.registrarSalida("ABC123");

        assertTrue(monto >= 20.0 && monto <= 40.0);
        verify(estanciaRepository).save(estancia);
    }


    @Test
    void testComenzarMes() {
        Vehiculo oficial = new Vehiculo();
        oficial.setPlaca("OFI1");
        oficial.setTipo(TipoVehiculo.OFICIAL);

        Vehiculo residente = new Vehiculo();
        residente.setPlaca("RES1");
        residente.setTipo(TipoVehiculo.RESIDENTE);
        residente.setMinutosAcumulados(1000);

        List<Vehiculo> vehiculos = List.of(oficial, residente);
        when(vehiculoRepository.findAll()).thenReturn(vehiculos);
        when(estanciaRepository.findByVehiculo(oficial)).thenReturn(List.of(new Estancia()));

        estacionamientoService.comenzarMes();

        verify(estanciaRepository).deleteAll(anyList());
        verify(vehiculoRepository).save(residente);
        assertEquals(0, residente.getMinutosAcumulados());
    }
}
