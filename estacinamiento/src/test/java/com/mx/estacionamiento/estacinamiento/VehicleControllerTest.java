package com.mx.estacionamiento.estacinamiento;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.estacionamiento.estacinamiento.controller.VehicleController;
import com.mx.estacionamiento.estacinamiento.model.Stay;
import com.mx.estacionamiento.estacinamiento.model.Vehicle;
import com.mx.estacionamiento.estacinamiento.model.VehicleType;
import com.mx.estacionamiento.estacinamiento.service.VehicleService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterVehicle() throws Exception {
        Vehicle v = new Vehicle();
        v.setPlate("RES123");
        v.setType(VehicleType.RESIDENTE);

        String json = objectMapper.writeValueAsString(v);

        mockMvc.perform(post("/api/vehicles/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Vehículo registrado"));

        verify(vehicleService, times(1))
                .registerVehicle(eq("RES123"), eq(VehicleType.RESIDENTE));
    }

    @Test
    void testRegisterEntry() throws Exception {
        Stay stay = new Stay();
        stay.setVehicle(new Vehicle());
        when(vehicleService.registerEntry("RES123")).thenReturn(stay);

        mockMvc.perform(post("/api/vehicles/RES123/entry"))
                .andExpect(status().isOk())
                .andExpect(content().string("Entrada registrada"));
    }

    @Test
    void testRegisterExit() throws Exception {
        when(vehicleService.registerExit("RES123")).thenReturn(50.0);

        mockMvc.perform(post("/api/vehicles/RES123/exit"))
                .andExpect(status().isOk())
                .andExpect(content().string("Salida registrada. Monto a pagar: 50.0"));

        verify(vehicleService, times(1)).registerExit("RES123");
    }

    @Test
    void testGenerateResidentReport() throws Exception {
        when(vehicleService.generateResidentReport()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/api/vehicles/residents/report"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(vehicleService, times(1)).generateResidentReport();
    }

    @Test
    void testResetMonth() throws Exception {
        mockMvc.perform(post("/api/vehicles/reset-month"))
                .andExpect(status().isOk())
                .andExpect(content().string("Datos reseteados para nuevo mes"));

        verify(vehicleService, times(1)).resetMonth();
    }

}
