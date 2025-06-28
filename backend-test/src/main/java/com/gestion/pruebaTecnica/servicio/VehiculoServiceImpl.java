package com.gestion.pruebaTecnica.servicio;

import com.gestion.pruebaTecnica.entidades.Vehiculo;
import com.gestion.pruebaTecnica.repositorios.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VehiculoServiceImpl implements VehiculoService {

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Vehiculo> findAll() {
		return (List<Vehiculo>) vehiculoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Vehiculo> findAll(Pageable pageable) {
		return vehiculoRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Vehiculo vehiculo) {
		vehiculoRepository.save(vehiculo);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		vehiculoRepository.deleteById(id);
	}

	@Override
	public Vehiculo existe(String placa) {
		Vehiculo tServ = vehiculoRepository.countExiste(placa);
		return tServ;
	}

	@Override
	@Transactional(readOnly = true)
	public Vehiculo findOne(Long id) {
		return vehiculoRepository.findById(id).orElse(null);
	}
}