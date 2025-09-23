package com.estacionamiento.app.estacionamiento.service;

import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;

public interface EntradaSalidaService {
	
	RegistroEntradaSalida save(RegistroEntradaSalida entradaSalida);
	
	RegistroEntradaSalida getByPlaca(String placa);
	RegistroEntradaSalida update(RegistroEntradaSalida entradaSalida);
	
	void deleteByTipo(String tipo);
	
	

}
