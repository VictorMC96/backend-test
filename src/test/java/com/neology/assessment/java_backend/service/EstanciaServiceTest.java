package com.neology.assessment.java_backend.service;

import com.neology.assessment.java_backend.controller.BusinessMessage;
import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Estancia;
import com.neology.assessment.java_backend.entity.TipoVehiculo;
import com.neology.assessment.java_backend.entity.Vehiculo;
import com.neology.assessment.java_backend.exception.BusinessException;
import com.neology.assessment.java_backend.repository.EstanciaRepository;
import com.neology.assessment.java_backend.repository.VehiculoRepository;
import com.neology.assessment.java_backend.service.impl.EstanciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstanciaServiceTest {

    @InjectMocks
    private EstanciaService estanciaService;

    @Mock
    private EstanciaRepository estanciaRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    private VehiculoRequest vehiculoResidenteRequest;
    private Vehiculo vehiculoResidente;
    private Estancia estancia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        vehiculoResidenteRequest = new VehiculoRequest();
        vehiculoResidenteRequest.setPlaca("ABC123");

        vehiculoResidente = new Vehiculo();
        vehiculoResidente.setPlaca("ABC123");
        vehiculoResidente.setFechaRegistro(Calendar.getInstance());

        TipoVehiculo tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setId(TipoVehiculoEnum.RESIDENTE.getCodigo()); // Supongamos que 1 es el código de residente
        tipoVehiculo.setTarifa(0.05);
        vehiculoResidente.setTipoVehiculo(tipoVehiculo);

        estancia = new Estancia();
        estancia.setVehiculo(vehiculoResidente);
        estancia.setFechaEntrada(Calendar.getInstance());
        estancia.setActivo(true);
        estancia.setMesActual(true);
    }

    @Test
    public void testRegistrarEntrada_VehiculoExistente_Residente_SinEstanciaActiva() {
        // Preparar datos
        when(vehiculoRepository.findByPlaca(vehiculoResidenteRequest.getPlaca())).thenReturn(Optional.of(vehiculoResidente));
        when(estanciaRepository.findByVehiculoAndFechaSalidaIsNull(vehiculoResidente)).thenReturn(Optional.empty());
        when(estanciaRepository.save(any(Estancia.class))).thenReturn(estancia);

        // Ejecutar el método
        Estancia result = estanciaService.registarEntrada(vehiculoResidenteRequest);

        // Verificar el comportamiento
        assertNotNull(result);
        assertEquals("ABC123", result.getVehiculo().getPlaca());
        verify(estanciaRepository, times(1)).save(any(Estancia.class));
    }

    @Test
    public void testRegistrarEntrada_VehiculoExistente_ConEstanciaActiva() {
        // Preparar datos
        when(vehiculoRepository.findByPlaca(vehiculoResidenteRequest.getPlaca())).thenReturn(Optional.of(vehiculoResidente));
        when(estanciaRepository.findByVehiculoAndFechaSalidaIsNull(vehiculoResidente)).thenReturn(Optional.of(estancia));

        // Ejecutar el método y verificar que se lanza la excepción
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            estanciaService.registarEntrada(vehiculoResidenteRequest);
        });

        assertEquals(BusinessMessage.ESTANCIA_ACTIVA, thrown.getBusinessMessage());
    }

    @Test
    public void testRegistrarEntrada_VehiculoNuevo() {
        // Preparar datos
        when(vehiculoRepository.findByPlaca(vehiculoResidenteRequest.getPlaca())).thenReturn(Optional.empty());
        when(estanciaRepository.save(any(Estancia.class))).thenReturn(estancia);

        // Ejecutar el método
        Estancia result = estanciaService.registarEntrada(vehiculoResidenteRequest);

        // Verificar el comportamiento
        assertNotNull(result);
        assertEquals("ABC123", result.getVehiculo().getPlaca());
        verify(estanciaRepository, times(1)).save(any(Estancia.class));
    }


    @Test
    public void testRegistrarSalida_VehiculoSinEstanciaActiva() {
        // Preparar datos
        when(vehiculoRepository.findByPlaca(vehiculoResidenteRequest.getPlaca())).thenReturn(Optional.of(vehiculoResidente));
        when(estanciaRepository.findByVehiculoAndFechaSalidaIsNull(vehiculoResidente)).thenReturn(Optional.empty());

        // Ejecutar el método y verificar que se lanza la excepción
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            estanciaService.registrarSalida(vehiculoResidenteRequest);
        });

        assertEquals(BusinessMessage.SIN_ESTANCIA_ACTIVA, thrown.getBusinessMessage());
    }
}
