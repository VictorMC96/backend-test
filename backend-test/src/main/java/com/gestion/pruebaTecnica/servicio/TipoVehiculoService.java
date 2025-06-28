package com.gestion.pruebaTecnica.servicio;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;

public interface TipoVehiculoService {

	public void save(TipoVehiculo tipoVehiculo);
	public TipoVehiculo existe(String placa);

}