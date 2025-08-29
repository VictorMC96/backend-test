package com.saul.prueba_tecnica.repositories;

import com.saul.prueba_tecnica.entities.TipoVehiculo;
import com.saul.prueba_tecnica.entities.Vehiculo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VehiculoRepositoryTest {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    void testGuardarYBuscarVehiculo() {
        Vehiculo v = new Vehiculo();
        v.setPlaca("ABC123");
        v.setTipo(TipoVehiculo.RESIDENTE);
        v.setMinutosAcumulados(100);

        vehiculoRepository.save(v);

        Optional<Vehiculo> encontrado = vehiculoRepository.findById("ABC123");

        assertTrue(encontrado.isPresent());
        assertEquals(TipoVehiculo.RESIDENTE, encontrado.get().getTipo());
        assertEquals(100, encontrado.get().getMinutosAcumulados());
    }
}
