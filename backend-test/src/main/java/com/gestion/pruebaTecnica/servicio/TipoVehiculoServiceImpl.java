package com.gestion.pruebaTecnica.servicio;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;
import com.gestion.pruebaTecnica.repositorios.TipoVehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoVehiculoServiceImpl implements TipoVehiculoService {

	@Autowired
	private TipoVehiculoRepository tipoVehiculoRepository;

	@Override
	@Transactional
	public void save(TipoVehiculo tipoVehiculo) {
		tipoVehiculoRepository.save(tipoVehiculo);
	}

	@Override
	public TipoVehiculo existe(String placa) {
		TipoVehiculo tServ = tipoVehiculoRepository.countExiste(placa);
		return tServ;
	}
}