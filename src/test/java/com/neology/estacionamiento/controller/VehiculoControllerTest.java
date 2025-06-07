package com.neology.estacionamiento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neology.estacionamiento.dto.PlacaDTO;
import com.neology.estacionamiento.model.Estancia;
import com.neology.estacionamiento.model.TipoVehiculo;
import com.neology.estacionamiento.model.Vehiculo;
import com.neology.estacionamiento.service.VehiculoService;
import com.neology.estacionamiento.util.Constantes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehiculoController.class)
class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehiculoService vehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlacaDTO request;

    @BeforeEach
    void setup() {
        request = new PlacaDTO();
        request.setPlaca("ABC123");
    }

    @Test
    void testRegistrarEntrada() throws Exception {
        when(vehiculoService.registrarEntrada("ABC123"))
            .thenReturn(new Estancia("ABC123", LocalDateTime.now()));

        mockMvc.perform(post("/estacionamiento/registro/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value(Constantes.ENTRADA_REGISTRADA));
    }

    @Test
    void testRegistrarSalida() throws Exception {
        when(vehiculoService.registrarSalida("ABC123"))
                .thenReturn("Salida registrada. Total a pagar: $5.00");

        mockMvc.perform(post("/estacionamiento/registro/salida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Salida registrada. Total a pagar: $5.00"));
    }

    @Test
    void testAltaVehiculoResidente() throws Exception {
        Vehiculo esperado = Vehiculo.builder()
                .placa("ABC123")
                .tipo(TipoVehiculo.RESIDENTE)
                .minutosAcumulados(0)
                .build();

        when(vehiculoService.registrarVehiculo(any(), eq(TipoVehiculo.RESIDENTE)))
                .thenReturn(esperado);

        mockMvc.perform(post("/estacionamiento/vehiculos/alta/residentes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("ABC123"))
                .andExpect(jsonPath("$.tipo").value("RESIDENTE"));
    }

    @Test
    void testResetMes() throws Exception {
        doNothing().when(vehiculoService).reiniciarMes();

        mockMvc.perform(post("/estacionamiento/comienza-mes"))
                .andExpect(status().isOk())
                .andExpect(content().string(Constantes.DATOS_REINICIADOS));
    }

    @Test
    void testGenerarInforme() throws Exception {
        List<Map<String, Object>> reporte = List.of(
                Map.of("placa", "ABC123", "minutos", 100, "total", "5.00")
        );
        when(vehiculoService.generarReporteResidentes()).thenReturn(reporte);
        when(vehiculoService.generarReporte(reporte, "reporte"))
                .thenReturn(Base64.getEncoder().encodeToString("fake_csv".getBytes()));

        mockMvc.perform(get("/estacionamiento/informe/pagos/residentes")
                .param("nombreArchivo", "reporte"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreArchivo").value("reporte.csv"))
                .andExpect(jsonPath("$.contenidoBase64").exists());
    }
}