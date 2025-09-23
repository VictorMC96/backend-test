package com.estacionamiento.app.estacionamiento.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;
import com.estacionamiento.app.estacionamiento.repository.EntradaSalidaRepository;
import com.estacionamiento.app.estacionamiento.service.EntradaSalidaService;

@Service
public class EntradaSalidaServiceImpl implements EntradaSalidaService{

	@Autowired
	EntradaSalidaRepository entradaSalidaRepository;

	@Override
	public RegistroEntradaSalida save(RegistroEntradaSalida entradaSalida) {
		// TODO Auto-generated method stub
		return entradaSalidaRepository.save(entradaSalida);
	}

	@Override
	public RegistroEntradaSalida getByPlaca(String placa) {
		// TODO Auto-generated method stub
		return entradaSalidaRepository.findByPlaca(placa);
	}

	@Override
	public RegistroEntradaSalida update(RegistroEntradaSalida entradaSalida) {
		// TODO Auto-generated method stub
		return entradaSalida;
	}

	@Override
	public void deleteByTipo(String tipo) {
		// TODO Auto-generated method stub
		entradaSalidaRepository.deleteRegistros(tipo);
	}
	
	
}
