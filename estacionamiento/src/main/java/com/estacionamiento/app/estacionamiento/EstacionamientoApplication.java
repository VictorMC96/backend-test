package com.estacionamiento.app.estacionamiento;
import com.estacionamiento.app.estacionamiento.serviceImp.CostoSeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.estacionamiento.app.estacionamiento.entities.Costo;
import com.estacionamiento.app.estacionamiento.entities.RegistroEntradaSalida;
import com.estacionamiento.app.estacionamiento.entities.Vehiculo;
import com.estacionamiento.app.estacionamiento.repository.CostoRepository;
import com.estacionamiento.app.estacionamiento.repository.EntradaSalidaRepository;
import com.estacionamiento.app.estacionamiento.repository.VehiculoRepository;
import com.estacionamiento.app.estacionamiento.service.CostoService;

@SpringBootApplication
public class EstacionamientoApplication {


	
	public static void main(String[] args) {
		SpringApplication.run(EstacionamientoApplication.class, args);
	}
	

	@Bean
    public CommandLineRunner cargarDatos(CostoRepository costoRepository, VehiculoRepository vehiculoRepository) {
        return args -> {
            // Datos de ejemplo
        	Costo costo= new Costo();
    		costo.setTipo("OFICIAL");
    		float costoOficial=0;
    		costo.setPrecio(costoOficial);
    		costoRepository.save(costo);

    		Costo costo2= new Costo();
    		costo2.setTipo("RESIDENTE");
    		float costoOficial2=(float) 0.05;
    		costo2.setPrecio(costoOficial2);
    		costoRepository.save(costo2);
            
    		Costo costo3= new Costo();
    		costo3.setTipo("NO RESIDENTE");
    		float costoOficial3=(float) 0.5;
    		costo3.setPrecio(costoOficial3);
    		costoRepository.save(costo3);
    		
    		Vehiculo vOficial= new Vehiculo();
    		vOficial.setPlaca("OFI01");
    		vOficial.setTipo(costo.getTipo());
    		
    		Vehiculo vOficial2= new Vehiculo();
    		vOficial2.setPlaca("OFI02");
    		vOficial2.setTipo(costo.getTipo());
    		
    		Vehiculo vOficial3= new Vehiculo();
    		vOficial3.setPlaca("OFI03");
    		vOficial3.setTipo(costo.getTipo());
    		
    		Vehiculo vOficial4= new Vehiculo();
    		vOficial4.setPlaca("OFI04");
    		vOficial4.setTipo(costo.getTipo());
    		
    		vehiculoRepository.save(vOficial);
    		vehiculoRepository.save(vOficial2);
    		vehiculoRepository.save(vOficial3);
    		vehiculoRepository.save(vOficial4);
    		
    		
    		Vehiculo vResidente= new Vehiculo();
    		vResidente.setPlaca("RES01");
    		vResidente.setTipo(costo2.getTipo());
    		vehiculoRepository.save(vResidente);
    		
    		Vehiculo vResidente2= new Vehiculo();
    		vResidente2.setPlaca("RES02");
    		vResidente2.setTipo(costo2.getTipo());
    		vehiculoRepository.save(vResidente2);
    		
    		Vehiculo vResidente3= new Vehiculo();
    		vResidente3.setPlaca("RES03");
    		vResidente3.setTipo(costo2.getTipo());
    		vehiculoRepository.save(vResidente3);
    		
    		Vehiculo vResidente4= new Vehiculo();
    		vResidente4.setPlaca("RES04");
    		vResidente4.setTipo(costo2.getTipo());
    		vehiculoRepository.save(vResidente4);

        };
    }

}
