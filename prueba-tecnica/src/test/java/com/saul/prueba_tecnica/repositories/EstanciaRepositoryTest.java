package com.saul.prueba_tecnica.repositories;

import com.saul.prueba_tecnica.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EstanciaRepositoryTest {

    @Autowired
    private EstanciaRepository estanciaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    void testBuscarPorVehiculoYUltimaEstancia() {
        Vehiculo v = new Vehiculo();
        v.setPlaca("TEST123");
        v.setTipo(TipoVehiculo.NO_RESIDENTE);
        vehiculoRepository.save(v);

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.HOUR, -3);
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.HOUR, -2);
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.MINUTE, -30);

        Estancia e1 = new Estancia();
        e1.setEntrada(cal1.getTime());
        e1.setSalida(cal2.getTime());
        e1.setVehiculo(v);

        Estancia e2 = new Estancia();
        e2.setEntrada(cal3.getTime());
        e2.setVehiculo(v);

        estanciaRepository.saveAll(List.of(e1, e2));

        List<Estancia> estancias = estanciaRepository.findByVehiculo(v);
        assertEquals(2, estancias.size());

        Estancia ultima = estanciaRepository.findTopByVehiculoOrderByEntradaDesc(v);
        assertEquals(e2.getEntrada(), ultima.getEntrada());
        assertNull(ultima.getSalida());
    }

}
