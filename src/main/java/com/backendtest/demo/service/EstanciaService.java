package com.backendtest.demo.service;

import com.backendtest.demo.entity.Estancia;
import com.backendtest.demo.entity.Vehiculo;
import com.backendtest.demo.repository.EstanciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class EstanciaService {

    @Autowired
    private EstanciaRepository estanciaRepository;

    public void registrarEntrada(Vehiculo vehiculo) {
        Estancia estancia = new Estancia();
        //estancia.setVehiculo(vehiculo);
        //estancia.setEntrada(Calendar.getInstance());
        estanciaRepository.save(estancia);
    }

    //public void registrarSalida(Vehiculo vehiculo) {
        // Estancia estancia = estanciaRepository.findFirstByVehiculoOrderByIdDesc(vehiculo);
        //if (estancia != null) {
        //   estancia.setSalida(Calendar.getInstance());
            // Lógica para el tipo de vehículo (oficial, residente, no residente)
            // Calculo de importe, etc.
    //estanciaRepository.save(estancia);
    // }
}


