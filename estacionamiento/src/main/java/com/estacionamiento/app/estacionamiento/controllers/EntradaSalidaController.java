package com.estacionamiento.app.estacionamiento.controllers;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estacionamiento.app.estacionamiento.dto.ResponseDto;
import com.estacionamiento.app.estacionamiento.entities.Costo;
import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;
import com.estacionamiento.app.estacionamiento.entities.TiempoAcumulado;
import com.estacionamiento.app.estacionamiento.entities.Vehiculo;
import com.estacionamiento.app.estacionamiento.service.CostoService;
import com.estacionamiento.app.estacionamiento.service.EntradaSalidaService;
import com.estacionamiento.app.estacionamiento.service.TiempoAcumuladoService;
import com.estacionamiento.app.estacionamiento.service.VehiculoService;
import com.estacionamiento.app.estacionamiento.serviceImp.VehiculoServiceImpl;

@RestController
@RequestMapping("api/registro")
public class EntradaSalidaController {

	@Autowired
	EntradaSalidaService entradaSalidaService;
	
	@Autowired
	CostoService costoService;
	
	@Autowired
    VehiculoServiceImpl serviceVehiculo;
	
	@Autowired
	TiempoAcumuladoService tiempoAcumuladoService;
	
	@PostMapping({"save"})
    public ResponseEntity<?> saveEntrada(@RequestParam String placa){
		 //System.out.println("placa ["+ placa+"]");
		Vehiculo vehiculo = serviceVehiculo.findByPlaca(placa);
		 System.out.println("vehiculo "+ vehiculo);
		String tipo="NO RESIDENTE";
		if(vehiculo !=null) {
		  tipo= vehiculo.getTipo();	
		  System.out.println("tipo "+ tipo);
		}
		
		//vehiculo.getPlaca();
		
		Costo costo= costoService.getByTipo(tipo);
		System.out.println("costo ["+ costo.getPrecio()+"]");
		
		RegistroEntradaSalida entradaSalida = new RegistroEntradaSalida();
		entradaSalida.setPlaca(placa);
		entradaSalida.setHoraEntrada(Calendar.getInstance());
		entradaSalida.setActivo(true);
		entradaSalida.setTipo(tipo);
		entradaSalida = entradaSalidaService.save(entradaSalida);
    	return ResponseEntity.status(HttpStatus.CREATED).body(entradaSalida);    	
    	
    }
	
	@PutMapping("salida")
	 public ResponseEntity<?> saveSalida(@RequestParam String placa){
		 //System.out.println("placa ["+ placa+"]");
		ResponseDto responseDto = new ResponseDto();
		RegistroEntradaSalida entradaSalida = entradaSalidaService.getByPlaca(placa);
		if(entradaSalida!=null) {
			Integer minutos = null;
			
			Calendar fechaInicio = entradaSalida.getHoraEntrada();
			Calendar fechaFin = Calendar.getInstance();
			entradaSalida.setHoraSalida(fechaFin);
			
			Vehiculo vehiculo = serviceVehiculo.findByPlaca(placa);
			String tipo= "";
			tipo = (vehiculo!=null)?vehiculo.getTipo():"NO RESIDENTE";
			
			
			Costo costo= costoService.getByTipo(tipo);
			Float total;
			if(!tipo.equals("OFICIAL")){
				minutos=difEnMinutos(fechaInicio,fechaFin);
			   	
		        
		     total=   (tipo.equals("RESIDENTE")) ? 0 : minutos*costo.getPrecio();
			}else {
				total=(float) 0;
			}
			if(tipo.equals("RESIDENTE")) {
				TiempoAcumulado tiempoAcumulado = new TiempoAcumulado();
				tiempoAcumulado.setPlaca(placa);
				tiempoAcumulado.setMinutos(minutos);
				tiempoAcumulado.setCosto(costo.getPrecio());
				tiempoAcumulado.setTotal(total);
				tiempoAcumuladoService.save(tiempoAcumulado);
			
			}
			entradaSalida.setMinuto(minutos);
			entradaSalida.setCosto(costo.getPrecio());
			entradaSalida.setTotal(total);
			entradaSalida.setActivo(false);
			entradaSalida = entradaSalidaService.save(entradaSalida);
			responseDto.setMsg("Se registro correctamente la salida del vehiculo con placas "+placa);

			
			
		}else {
			responseDto.setMsg("No se encuentra registrado el vehículo con placas: "+placa);
		}
		
		
		   	return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);    	
   	
   }
	
	
	
	private  int difEnMinutos(Calendar fechaInicial, Calendar fechaFInal) {
		long millisInicio = fechaInicial.getTimeInMillis();
        long millisFin = fechaFInal.getTimeInMillis();
        
        long diferencia = Math.abs(millisFin - millisInicio);
        long segundos = diferencia/1000;
        Integer minutos = (int) (segundos/60); 
        System.out.println("diferencia "+diferencia);
        System.out.println("segundos "+segundos);
        System.out.println("minutos "+minutos);
		return minutos;
	}
}
