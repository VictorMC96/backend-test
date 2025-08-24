package com.neology.estacionamiento;

import com.neology.estacionamiento.model.Estancia;
import com.neology.estacionamiento.model.VehiculoNoResidente;
import com.neology.estacionamiento.model.VehiculoOficial;
import com.neology.estacionamiento.model.VehiculoResidente;
import com.neology.estacionamiento.repository.EstanciaRepository;
import com.neology.estacionamiento.repository.VehiculoNoResidenteRepository;
import com.neology.estacionamiento.repository.VehiculoOficialRepository;
import com.neology.estacionamiento.repository.VehiculoResidenteRepository;
import com.neology.estacionamiento.service.EstacionamientoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstacionamientoServiceTest {
    
    @Mock
    private EstanciaRepository estanciaRepository;
    @Mock
    private VehiculoOficialRepository vehiculoOficialRepository;
    @Mock
    private VehiculoResidenteRepository vehiculoResidenteRepository;
    @Mock
    private VehiculoNoResidenteRepository vehiculoNoResidenteRepository;

    @InjectMocks
    private EstacionamientoService estacionamientoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegistrarSalida_NoResidente_CalculaCostoCorrectamente() {
        // Arrange
        String placa = "ABCD123";
        VehiculoNoResidente vehiculo = new VehiculoNoResidente();
        vehiculo.setPlaca(placa);

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);
        
        Calendar horaEntrada = Calendar.getInstance();
        horaEntrada.add(Calendar.MINUTE, -60); // 60 minutos antes
        estancia.setHoraEntrada(horaEntrada);

        when(estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa)).thenReturn(Optional.of(estancia));

        // Act
        double costo = estacionamientoService.registrarSalida(placa);

        // Assert
        assertEquals(30.0, costo, 0.01); // 60 minutos * 0.5 = 30
        verify(estanciaRepository, times(1)).findByVehiculoPlacaAndHoraSalidaIsNull(placa);
        verify(estanciaRepository, times(1)).save(estancia);
    }

    @Test
    void registrarSalida_residente_acumulaTiempoCorrectamente() {
        // 1. Arrange
        String placa = "R3S1D3NT3";
        VehiculoResidente vehiculo = new VehiculoResidente();
        vehiculo.setPlaca(placa);
        vehiculo.setTiempoAcumuladoMinutos(100); // Ya tenía tiempo acumulado

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);

        // Simular 60 minutos más de estacionamiento
        Calendar horaEntrada = Calendar.getInstance();
        horaEntrada.add(Calendar.MINUTE, -60);
        estancia.setHoraEntrada(horaEntrada);

        when(estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa))
            .thenReturn(Optional.of(estancia));
        
        // 2. Act
        double costo = estacionamientoService.registrarSalida(placa);

        // 3. Assert
        assertEquals(0.0, costo, "Los residentes no deben pagar a la salida");
        assertEquals(160, vehiculo.getTiempoAcumuladoMinutos(), "El tiempo no se acumuló correctamente");
        assertNotNull(estancia.getHoraSalida(), "La hora de salida no fue registrada");

        // Verificar que los repositorios fueron llamados
        verify(estanciaRepository, times(1)).findByVehiculoPlacaAndHoraSalidaIsNull(placa);
        verify(vehiculoResidenteRepository, times(1)).save(vehiculo); // Verificamos que el residente se guardó con el nuevo tiempo
        verify(estanciaRepository, times(1)).save(estancia);
    }

    @Test
    void registrarSalida_oficial_noTieneCosto() {
        // 1. Arrange
        String placa = "0F1C1AL";
        VehiculoOficial vehiculo = new VehiculoOficial();
        vehiculo.setPlaca(placa);

        Estancia estancia = new Estancia();
        estancia.setVehiculo(vehiculo);

        when(estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull(placa))
            .thenReturn(Optional.of(estancia));

        // 2. Act
        double costo = estacionamientoService.registrarSalida(placa);

        // 3. Assert
        assertEquals(0.0, costo, "Los vehículos oficiales no deben pagar");
        verify(estanciaRepository, times(1)).save(estancia);
    }
}
