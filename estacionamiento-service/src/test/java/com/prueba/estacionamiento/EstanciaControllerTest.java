package com.prueba.estacionamiento;


import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.prueba.estacionamiento.controller.EstanciaController;
import com.prueba.estacionamiento.model.Estancia;
import com.prueba.estacionamiento.model.TarifaRequest;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.service.EstanciaService;
import com.prueba.estacionamiento.service.PagoService;
import com.prueba.estacionamiento.service.TarifaService;
import com.prueba.estacionamiento.service.VehiculoService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EstanciaController.class)
public class EstanciaControllerTest {
	
	
	 @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private VehiculoService vehiculoService;

	    @MockBean
	    private EstanciaService estanciaService;

	    @MockBean
	    private TarifaService tarifaService;

	    @MockBean
	    private PagoService pagoService;

	    @Test
	    public void testRegistrarEntrada() throws Exception {
	        Estancia mockEstancia = new Estancia();
	        mockEstancia.setEntrada(Calendar.getInstance());

	        when(estanciaService.registrarEntrada("ABC123")).thenReturn(mockEstancia);

	        mockMvc.perform(post("/estancia/entrada")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"placa\":\"ABC123\"}"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.mensaje").value("Entrada registrada"));
	    }

	    @Test
	    public void testRegistrarSalidaNoResidente() throws Exception {
	        Estancia mockEstancia = new Estancia();
	        Calendar entrada = Calendar.getInstance();
	        entrada.add(Calendar.MINUTE, -30);
	        Calendar salida = Calendar.getInstance();
	        mockEstancia.setEntrada(entrada);
	        mockEstancia.setSalida(salida);

	        when(vehiculoService.obtenerTipoPorPlaca("XYZ999")).thenReturn(TipoVehiculo.NO_RESIDENTE);
	        when(estanciaService.registrarSalida("XYZ999")).thenReturn(mockEstancia);
	        when(tarifaService.calcularTarifa(any(TarifaRequest.class))).thenReturn(new BigDecimal("15.00"));

	        mockMvc.perform(post("/estancia/salida")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"placa\":\"XYZ999\"}"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.mensaje").value("Pago registrado"))
	                .andExpect(jsonPath("$.monto").value(15.0));
	    }

	    @Test
	    public void testAltaVehiculoOficial() throws Exception {
	        mockMvc.perform(post("/estancia/alta/oficial")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"placa\":\"OFI111\"}"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Vehículo oficial registrado"));
	    }

	    @Test
	    public void testComenzarMes() throws Exception {
	        mockMvc.perform(post("/estancia/comienza-mes"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Nuevo mes iniciado. Estancias y tiempos reiniciados."));
	    }

	    @Test
	    public void testGenerarInformePagosResidentes() throws Exception {
	        mockMvc.perform(post("/estancia/pagos/residentes")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content("{\"archivo\":\"informe.csv\"}"))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Informe generado: informe.csv"));
	    }

}
