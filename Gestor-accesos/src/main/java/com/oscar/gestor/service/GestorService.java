package com.oscar.gestor.service;

public interface GestorService {
	public void registrarEntrada(String placa);

	public void registrarSalida(String placa);
	
	public void registarAuto(String placa,int tipo);

}
