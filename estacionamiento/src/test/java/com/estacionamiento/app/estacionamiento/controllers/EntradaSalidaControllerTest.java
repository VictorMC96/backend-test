package com.estacionamiento.app.estacionamiento.controllers;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.estacionamiento.app.estacionamiento.entities.Costo;
import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;
import com.estacionamiento.app.estacionamiento.entities.Vehiculo;
import com.estacionamiento.app.estacionamiento.service.CostoService;
import com.estacionamiento.app.estacionamiento.service.EntradaSalidaService;
import com.estacionamiento.app.estacionamiento.service.TiempoAcumuladoService;
import com.estacionamiento.app.estacionamiento.serviceImp.VehiculoServiceImpl;

public class EntradaSalidaControllerTest {

	
	@Mock
    private EntradaSalidaService entradaSalidaService;

    @Mock
    private CostoService costoService;

    @Mock
    private VehiculoServiceImpl serviceVehiculo;

    @Mock
    private TiempoAcumuladoService tiempoAcumuladoService;

    @InjectMocks
    private EntradaSalidaController entradaSalidaController;
    
    private Vehiculo vehiculoResidente;
    private Vehiculo vehiculoOficial;
    private Vehiculo vehiculoNoResidente;
    private Costo costoResidente;
    private Costo costoNoResidente;
    private Costo costoOficial;
    private RegistroEntradaSalida registroEntradaSalida;
    private Calendar horaEntrada;
    private Calendar horaSalida;
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entradaSalidaController).build();

        // Configurar datos de prueba
        horaEntrada = Calendar.getInstance();
        horaSalida = Calendar.getInstance();
        horaSalida.add(Calendar.HOUR, 2); // 2 horas después

        vehiculoResidente = new Vehiculo();
        vehiculoResidente.setPlaca("RES01");
        vehiculoResidente.setTipo("RESIDENTE");

        vehiculoOficial = new Vehiculo();
        vehiculoOficial.setPlaca("OFI01");
        vehiculoOficial.setTipo("OFICIAL");

        vehiculoNoResidente = new Vehiculo();
        vehiculoNoResidente.setPlaca("NR001");
        vehiculoNoResidente.setTipo("NO RESIDENTE");

        costoResidente = new Costo();
        costoResidente.setTipo("RESIDENTE");
        Float precio =(float) 0.05;
        costoResidente.setPrecio(precio);

        costoNoResidente = new Costo();
        costoNoResidente.setTipo("NO RESIDENTE");
        Float precio2 =(float) 0.5;
        costoNoResidente.setPrecio(precio2);

        costoOficial = new Costo();
        costoOficial.setTipo("OFICIAL");
        Float precio3 =(float) 0;
        costoOficial.setPrecio(precio3);

        registroEntradaSalida = new RegistroEntradaSalida();
        registroEntradaSalida.setId(1L);
        registroEntradaSalida.setPlaca("RES02");
        registroEntradaSalida.setHoraEntrada(horaEntrada);
        registroEntradaSalida.setActivo(true);
    }

    @Test
    void testSaveEntrada_VehiculoResidente_DeberiaRegistrarEntrada() throws Exception {
        // Arrange
        when(serviceVehiculo.findByPlaca("ABC123")).thenReturn(vehiculoResidente);
        when(costoService.getByTipo("RESIDENTE")).thenReturn(costoResidente);
        when(entradaSalidaService.save((RegistroEntradaSalida) any(RegistroEntradaSalida.class))).thenReturn(registroEntradaSalida);

        // Act & Assert
        mockMvc.perform(post("/api/registro/save")
                .param("placa", "RES01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.placa").value("ABC123"))
                .andExpect((ResultMatcher) jsonPath("$.activo").value(true));
    }
}
