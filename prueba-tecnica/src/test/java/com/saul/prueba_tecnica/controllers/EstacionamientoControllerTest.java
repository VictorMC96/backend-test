package com.saul.prueba_tecnica.controllers;

import com.saul.prueba_tecnica.services.EstacionamientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstacionamientoController.class)
public class EstacionamientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstacionamientoService estacionamientoService;

    @Test
    void testAltaVehiculoOficial() throws Exception {
        doNothing().when(estacionamientoService).registrarVehiculoOficial("XYZ123");

        mockMvc.perform(post("/api/estacionamiento/alta/oficial/XYZ123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehículo oficial registrado"));
    }

    @Test
    void testRegistrarEntrada() throws Exception {
        doNothing().when(estacionamientoService).registrarEntrada("XYZ123");

        mockMvc.perform(post("/api/estacionamiento/entrada/XYZ123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Entrada registrada"));
    }

    @Test
    void testRegistrarSalidaResidente() throws Exception {
        when(estacionamientoService.registrarSalida("XYZ123")).thenReturn(0.0);

        mockMvc.perform(post("/api/estacionamiento/salida/XYZ123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Salida registrada"));
    }

    @Test
    void testRegistrarSalidaNoResidente() throws Exception {
        when(estacionamientoService.registrarSalida("ABC999")).thenReturn(25.0);

        mockMvc.perform(post("/api/estacionamiento/salida/ABC999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Monto a pagar: $25.0"));
    }

    @Test
    void testComenzarMes() throws Exception {
        doNothing().when(estacionamientoService).comenzarMes();

        mockMvc.perform(post("/api/estacionamiento/comenzar-mes"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mes iniciado. Estancias oficiales borradas y residentes reiniciados."));
    }

    @Test
    void testReporteResidentes() throws Exception {

        doNothing().when(estacionamientoService).generarReportePagos();

        mockMvc.perform(get("/api/estacionamiento/reportes/residentes"))
                .andExpect(status().isOk())
                .andExpect(content().string("Archivo generado correctamente."));
    }
}
