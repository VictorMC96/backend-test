package com.ejercicio.estacionamiento.service;

import com.ejercicio.estacionamiento.model.Estancia;
import com.ejercicio.estacionamiento.model.Vehiculo;
import com.ejercicio.estacionamiento.repository.EstanciaRepository;
import com.ejercicio.estacionamiento.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EstacionamientoServiceTest {

    @Autowired
    private EstacionamientoService estacionamientoService;

    @MockBean
    private VehiculoRepository vehiculoRepository;

    @MockBean
    private EstanciaRepository estanciaRepository;

    @Test
    public void testRegistrarEntrada() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPlaca("ABC123");
        vehiculo.setTipo("residente");

        Mockito.when(vehiculoRepository.findByPlaca("ABC123")).thenReturn(Optional.of(vehiculo));

        Estancia estancia = estacionamientoService.registrarEntrada("ABC123");
        assertNotNull(estancia);
        assertEquals(vehiculo, estancia.getVehiculo());

        // esta línea verifica que la hora de entrada se haya registrado
        assertNotNull(estancia.getHoraEntrada());
    }

    @Test
    public void testRegistrarSalida() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPlaca("ABC123");
        vehiculo.setTipo("residente");

        Estancia estancia = new Estancia();
        estancia.setId(1L);
        Calendar entrada = Calendar.getInstance();
        entrada.add(Calendar.HOUR, -1);
        estancia.setHoraEntrada(entrada);
        estancia.setVehiculo(vehiculo);

        Mockito.when(estanciaRepository.findByVehiculoPlacaAndHoraSalidaIsNull("ABC123")).thenReturn(Optional.of(estancia));
        Mockito.when(vehiculoRepository.save(Mockito.any(Vehiculo.class))).thenReturn(vehiculo);

        Estancia salida = estacionamientoService.registrarSalida("ABC123");
        assertNotNull(salida);
        assertNotNull(salida.getHoraSalida());
        assertEquals(60, vehiculo.getTiempoAcumulado());
    }

    @Test
    public void testGenerarInformePagosResidentes() {
        Vehiculo vehiculo1 = new Vehiculo();
        vehiculo1.setPlaca("S1234A");
        vehiculo1.setTipo("residente");
        vehiculo1.setTiempoAcumulado(20134);

        Vehiculo vehiculo2 = new Vehiculo();
        vehiculo2.setPlaca("4567ABC");
        vehiculo2.setTipo("residente");
        vehiculo2.setTiempoAcumulado(4896);

        Mockito.when(vehiculoRepository.findByTipo("residente")).thenReturn(List.of(vehiculo1, vehiculo2));

        String informe = estacionamientoService.generarInformePagosResidentes("informe.txt");
        assertEquals("Informe generado correctamente en informe.txt", informe);

        // revisa que el contenido del informe sea correcto
        String contenidoEsperado = "Núm. placa\tTiempo estacionado (min.)\tCantidad a pagar\n"
                + "S1234A\t20134\t1006.70\n"
                + "4567ABC\t4896\t244.80\n";

        // revisa el archivo txt
        try {
            String contenidoArchivo = new String(Files.readAllBytes(Paths.get("informe.txt")), StandardCharsets.UTF_8);
            assertEquals(contenidoEsperado, contenidoArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
