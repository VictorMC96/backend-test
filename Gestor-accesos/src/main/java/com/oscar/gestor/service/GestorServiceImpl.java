package com.oscar.gestor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.gestor.repository.IAutoRepository;
import com.oscar.gestor.repository.ILugarRepository;
import com.oscar.gestor.repository.entity.Auto;
import com.oscar.gestor.repository.entity.LugarEntity;

@Service
public class GestorServiceImpl implements GestorService {
	
	@Autowired
	ILugarRepository repo;
	
	@Autowired
	IAutoRepository autoRepository;

	@Override
	public void registrarEntrada(String placa) {
		
		LugarEntity entity = new LugarEntity();
		repo.save(entity);
		
		
	}

	@Override
	public void registrarSalida(String placa) {
		//TODO HACER PETICION DE BUSQUEDA POR PLACA
		
	}

	@Override
	public void registarAuto(String placa, int tipo) {
		Auto auto = new Auto();
		autoRepository.save(auto);
		
	}

}
