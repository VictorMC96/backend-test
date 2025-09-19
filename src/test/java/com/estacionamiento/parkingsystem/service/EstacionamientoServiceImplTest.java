package com.estacionamiento.parkingsystem.service;

import com.estacionamiento.parkingsystem.model.*;
import com.estacionamiento.parkingsystem.repository.EstanciaRepository;
import com.estacionamiento.parkingsystem.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class EstacionamientoServiceImplTest {
    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private EstanciaRepository estanciaRepository;

    @InjectMocks
    private EstacionamientoServiceImpl estacionamientoService;

    private VehiculoOficial vehiculoOficial;
    private VehiculoResidente vehiculoResidente;
    private VehiculoNoResidente vehiculoNoResidente;
    private Estancia estancia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehiculoOficial = new VehiculoOficial("OFI123");
        vehiculoOficial.setTipo(TipoVehiculo.OFICIAL);

        vehiculoResidente = new VehiculoResidente("RES123");
        vehiculoResidente.setTipo(TipoVehiculo.RESIDENTE);

        vehiculoNoResidente = new VehiculoNoResidente("NOR123");
        vehiculoNoResidente.setTipo(TipoVehiculo.NO_RESIDENTE);

        estancia = new Estancia();
        estancia.setVehiculo(vehiculoNoResidente);
        estancia.setHoraEntrada(LocalDateTime.now().minusMinutes(60)); // 60 min estacionado
    }


    @Test
    void testRegistrarSalida_NoResidente() {
        when(vehiculoRepository.findByPlaca("NOR123")).thenReturn(Optional.of(vehiculoNoResidente));
        when(estanciaRepository.findFirstByVehiculoAndHoraSalidaIsNullOrderByHoraEntradaDesc(vehiculoNoResidente))
                .thenReturn(estancia);

        double importe = estacionamientoService.registrarSalida("NOR123");

        assertEquals(60 * 0.5, importe, 0.01); // 60 minutos * $0.5/minuto
        verify(estanciaRepository, times(1)).save(estancia);
    }


    @Test
    void testRegistrarSalida_Residente() {

        estancia.setVehiculo(vehiculoResidente);
        when(vehiculoRepository.findByPlaca("RES123")).thenReturn(Optional.of(vehiculoResidente));
        when(estanciaRepository.findFirstByVehiculoAndHoraSalidaIsNullOrderByHoraEntradaDesc(vehiculoResidente))
                .thenReturn(estancia);

        double importe = estacionamientoService.registrarSalida("RES123");

        assertEquals(0.0, importe, 0.01); // No se paga al salir
        assertEquals(60, vehiculoResidente.getTiempoAcumuladoMinutos()); // Se acumula tiempo
        verify(estanciaRepository, times(1)).save(estancia);
    }


    @Test
    void testRegistrarSalida_Oficial() {
        estancia.setVehiculo(vehiculoOficial);
        when(vehiculoRepository.findByPlaca("OFI123")).thenReturn(Optional.of(vehiculoOficial));
        when(estanciaRepository.findFirstByVehiculoAndHoraSalidaIsNullOrderByHoraEntradaDesc(vehiculoOficial))
                .thenReturn(estancia);

        double importe = estacionamientoService.registrarSalida("OFI123");

        assertEquals(0.0, importe, 0.01); // Oficial no paga
        verify(estanciaRepository, times(1)).save(estancia);
    }


    @Test
    void testRegistrarEntrada() {
        when(vehiculoRepository.findByPlaca("NOR123")).thenReturn(Optional.of(vehiculoNoResidente));

        estacionamientoService.registrarEntrada("NOR123");

        verify(estanciaRepository, times(1)).save(any(Estancia.class));
    }


    @Test
    void testDarDeAltaVehiculoOficial() {
        estacionamientoService.darDeAltaVehiculoOficial("OFI999");

        verify(vehiculoRepository, times(1)).save(any(VehiculoOficial.class));
    }



    @Test
    void testDarDeAltaVehiculoResidente() {
        estacionamientoService.darDeAltaVehiculoResidente("RES999");

        verify(vehiculoRepository, times(1)).save(any(VehiculoResidente.class));
    }

    @Test
    void testComenzarMes() {
        List<VehiculoOficial> oficiales = List.of(vehiculoOficial);
        List<VehiculoResidente> residentes = List.of(vehiculoResidente);

        when(vehiculoRepository.findAllOficiales()).thenReturn(oficiales);
        when(vehiculoRepository.findAllResidentes()).thenReturn(residentes);
        when(estanciaRepository.findAllByVehiculo(vehiculoOficial)).thenReturn(List.of(estancia));

        vehiculoResidente.setTiempoAcumuladoMinutos(120);

        estacionamientoService.comenzarMes();

        verify(estanciaRepository, times(1)).deleteAll(List.of(estancia));
        assertEquals(0, vehiculoResidente.getTiempoAcumuladoMinutos());
        verify(vehiculoRepository, times(1)).save(vehiculoResidente);
    }

    @Test
    void testGenerarInformePagos() throws IOException {
        vehiculoResidente.setTiempoAcumuladoMinutos(200);
        when(vehiculoRepository.findAllResidentes()).thenReturn(List.of(vehiculoResidente));

        String archivo = "test_informe.txt";
        estacionamientoService.generarInformePagos(archivo);

        List<String> lineas = Files.readAllLines(Paths.get(archivo));

        assertEquals(2, lineas.size()); // cabecera + 1 registro
        assertTrue(lineas.get(0).contains("Núm. placa"));
        assertTrue(lineas.get(1).contains("RES123"));
        assertTrue(lineas.get(1).contains("200"));
        assertTrue(lineas.get(1).contains(String.format("%.2f", 200 * 0.05)));

        Files.delete(Paths.get(archivo));
    }


}
