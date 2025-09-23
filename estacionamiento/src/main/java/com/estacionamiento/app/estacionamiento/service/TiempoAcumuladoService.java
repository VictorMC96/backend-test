package com.estacionamiento.app.estacionamiento.service;

import java.util.List;

import com.estacionamiento.app.estacionamiento.dto.ResidentesInformeDto;
import com.estacionamiento.app.estacionamiento.dto.ResponseDto;
import com.estacionamiento.app.estacionamiento.entities.TiempoAcumulado;

public interface TiempoAcumuladoService {

	TiempoAcumulado save(TiempoAcumulado tiempoAcumulado);
	
	TiempoAcumulado getByPlaca(String placa);
	
	ResponseDto reiniciaMinutos(String placa); 
	
	List<ResidentesInformeDto> listResidentes();
	
}
