package com.estacionamiento.app.estacionamiento.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamiento.app.estacionamiento.entities.Vehiculo;
import com.estacionamiento.app.estacionamiento.repository.VehiculoRepository;
import com.estacionamiento.app.estacionamiento.service.VehiculoService;

@Service
public class VehiculoServiceImpl implements VehiculoService{

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo save(Vehiculo vehiculo) {

            return vehiculoRepository.save(vehiculo);
    }

	@Override
	public Vehiculo findByPlaca(String placa) {
		// TODO Auto-generated method stub
		return vehiculoRepository.findByPlaca(placa);
	}

}
