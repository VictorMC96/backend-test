package com.estacionamiento.app.estacionamiento.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamiento.app.estacionamiento.entities.Costo;
import com.estacionamiento.app.estacionamiento.repository.CostoRepository;
import com.estacionamiento.app.estacionamiento.service.CostoService;

@Service
public class CostoSeviceImpl implements CostoService{

    @Autowired
    CostoRepository costoRepository;

    @Override
    public Costo save(Costo costo) {

      return costoRepository.save(costo);

    }

	@Override
	public Costo getByTipo(String tipo) {
		// TODO Auto-generated method stub
		return costoRepository.findByTipo(tipo);
	}

	
}
