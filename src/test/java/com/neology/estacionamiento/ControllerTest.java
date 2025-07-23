package com.neology.estacionamiento;

import com.neology.estacionamiento.application.service.*;
import com.neology.estacionamiento.domain.model.*;
import com.neology.estacionamiento.web.controller.*;
import com.neology.estacionamiento.web.request.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para todos los controllers y endpoints REST
 */
@WebMvcTest(controllers = {EstacionamientoController.class, VehiculoController.class, FacturacionController.class})
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IEstacionamientoService estacionamientoService;

    @MockBean
    private IVehiculoService vehiculoService;

    @MockBean
    private IFacturacionService facturacionService;

    // =====================================
    // CONTROLLER TESTS: EstacionamientoController
    // =====================================

    @Test
    void casoUso_RegistrarEntrada_REST_Exitoso() throws Exception {
        // Given
        String placa = "ABC123";
        VehiculoNoResidente vehiculo = new VehiculoNoResidente(placa);
        Estancia estancia = new Estancia(vehiculo);
        when(estacionamientoService.registrarEntrada(placa)).thenReturn(estancia);

        // When & Then
        mockMvc.perform(post("/api/estacionamiento/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new EntradaRequest(placa))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Entrada registrada exitosamente"))
                .andExpect(jsonPath("$.data.placa").value(placa))
                .andExpect(jsonPath("$.data.tipoVehiculo").value("NO_RESIDENTE"))
                .andExpect(jsonPath("$.data.fechaEntrada").exists());

        verify(estacionamientoService).registrarEntrada(placa);
    }

    @Test
    void casoUso_RegistrarEntrada_REST_ErrorVehiculoActivo() throws Exception {
        // Given
        String placa = "GOV001";
        when(estacionamientoService.registrarEntrada(placa))
            .thenThrow(new IllegalStateException("El vehículo GOV001 ya tiene una estancia activa"));

        // When & Then
        mockMvc.perform(post("/api/estacionamiento/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new EntradaRequest(placa))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Estado inválido")));
    }

    @Test
    void casoUso_ComenzarMes_REST_Exitoso() throws Exception {
        // Given
        doNothing().when(estacionamientoService).iniciarNuevoMes();

        // When & Then
        mockMvc.perform(post("/api/estacionamiento/nuevo-mes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Nuevo mes iniciado exitosamente"))
                .andExpect(jsonPath("$.data", containsString("eliminado todas las estancias")));

        verify(estacionamientoService).iniciarNuevoMes();
    }

    // =====================================
    // CONTROLLER TESTS: VehiculoController
    // =====================================

    @Test
    void casoUso_AltaVehiculoOficial_REST_Exitoso() throws Exception {
        // Given
        String placa = "GOV123";
        VehiculoOficial vehiculo = new VehiculoOficial(placa);
        when(vehiculoService.registrarVehiculoOficial(placa)).thenReturn(vehiculo);

        // When & Then
        mockMvc.perform(post("/api/vehiculos/oficial")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegistroVehiculoRequest(placa))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Vehículo oficial registrado exitosamente")))
                .andExpect(jsonPath("$.data.placa").value(placa))
                .andExpect(jsonPath("$.data.tipo").value("OFICIAL"))
                .andExpect(jsonPath("$.data.descripcionTipo", containsString("Vehículo oficial")));

        verify(vehiculoService).registrarVehiculoOficial(placa);
    }

    @Test
    void casoUso_AltaVehiculoOficial_REST_ErrorPlacaDuplicada() throws Exception {
        // Given
        String placa = "GOV123";
        when(vehiculoService.registrarVehiculoOficial(placa))
            .thenThrow(new IllegalArgumentException("Ya existe un vehículo registrado con la placa: " + placa));

        // When & Then
        mockMvc.perform(post("/api/vehiculos/oficial")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegistroVehiculoRequest(placa))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Ya existe un vehículo registrado")));
    }

    @Test
    void casoUso_AltaVehiculoResidente_REST_Exitoso() throws Exception {
        // Given
        String placa = "RES123";
        VehiculoResidente vehiculo = new VehiculoResidente(placa);
        when(vehiculoService.registrarVehiculoResidente(placa)).thenReturn(vehiculo);

        // When & Then
        mockMvc.perform(post("/api/vehiculos/residente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegistroVehiculoRequest(placa))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Vehículo de residente registrado exitosamente")))
                .andExpect(jsonPath("$.data.placa").value(placa))
                .andExpect(jsonPath("$.data.tipo").value("RESIDENTE"))
                .andExpect(jsonPath("$.data.tiempoAcumuladoMes").value(0));

        verify(vehiculoService).registrarVehiculoResidente(placa);
    }

    // =====================================
    // CONTROLLER TESTS: FacturacionController 
    // =====================================

    @Test
    void casoUso_PagosResidentes_REST_ArchivoPersonalizado() throws Exception {
        // Given
        String nombreArchivo = "reporte_enero_2025";
        FacturacionService.ItemReporteResidente item = 
            new FacturacionService.ItemReporteResidente("S1234A", 20134, new BigDecimal("1006.70"));
        
        List<FacturacionService.ItemReporteResidente> items = Arrays.asList(item);
        FacturacionService.ReporteResidentes reporte = 
            new FacturacionService.ReporteResidentes(items, new BigDecimal("1006.70"), new Date());
        
        when(facturacionService.generarReporteResidentes()).thenReturn(reporte);

        // When & Then
        mockMvc.perform(post("/api/facturacion/residentes/generar-informe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ReporteRequest(nombreArchivo))))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain; charset=utf-8"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + nombreArchivo + ".txt"))
                .andExpect(content().string(containsString("Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar")))
                .andExpect(content().string(containsString("S1234A\t20134\t\t1006.70")));

        verify(facturacionService).generarReporteResidentes();
    }

    @Test
    void casoUso_PagosResidentes_REST_ArchivoError() throws Exception {
        // Given - Nombre de archivo vacío
        ReporteRequest request = new ReporteRequest("");

        // When & Then
        mockMvc.perform(post("/api/facturacion/residentes/generar-informe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("El nombre del archivo es requerido")));

        verify(facturacionService, never()).generarReporteResidentes();
    }
} 