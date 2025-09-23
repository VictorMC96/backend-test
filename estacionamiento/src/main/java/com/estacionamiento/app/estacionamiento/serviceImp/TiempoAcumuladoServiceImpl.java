package com.estacionamiento.app.estacionamiento.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estacionamiento.app.estacionamiento.dto.ResidentesInformeDto;
import com.estacionamiento.app.estacionamiento.dto.ResponseDto;
import com.estacionamiento.app.estacionamiento.entities.TiempoAcumulado;
import com.estacionamiento.app.estacionamiento.repository.TiempoAcumuladoRepository;
import com.estacionamiento.app.estacionamiento.service.TiempoAcumuladoService;

@Service
public class TiempoAcumuladoServiceImpl implements TiempoAcumuladoService{

	@Autowired
	TiempoAcumuladoRepository tiempoAcumuladorepository; 
	
	@Override
	public TiempoAcumulado save(TiempoAcumulado tiempoAcumulado) {
		// TODO Auto-generated method stub
		TiempoAcumulado vehiculoAcumulado=tiempoAcumuladorepository.findByPlaca(tiempoAcumulado.getPlaca());
		if( vehiculoAcumulado != null) {
			Integer tiempo = vehiculoAcumulado.getMinutos()+tiempoAcumulado.getMinutos();
			Float total=tiempo*vehiculoAcumulado.getCosto();
			vehiculoAcumulado.setMinutos(tiempo);
			vehiculoAcumulado.setTotal(total);
			return tiempoAcumuladorepository.save(vehiculoAcumulado);
		}
		Float total= tiempoAcumulado.getCosto()*tiempoAcumulado.getMinutos();
		tiempoAcumulado.setTotal(total);
		return tiempoAcumuladorepository.save(tiempoAcumulado);
	}

	@Override
	public TiempoAcumulado getByPlaca(String placa) {
		// TODO Auto-generated method stub
		return tiempoAcumuladorepository.findByPlaca(placa);
	}

	@Override
	public ResponseDto reiniciaMinutos(String placa) {
		// TODO Auto-generated method stub
		/*TiempoAcumulado tiempoAcumulado=tiempoAcumuladorepository.findByPlaca(placa);
		tiempoAcumulado.setMinutos(0);
		Float total=(float) 0;
		tiempoAcumulado.setTotal(total);
		
		tiempoAcumulado=tiempoAcumuladorepository.save(tiempoAcumulado);*/
		
		tiempoAcumuladorepository.updateTiempo(0,0);
		ResponseDto responseDto= new  ResponseDto();
		responseDto.setMsg("OK");
		return responseDto;
	}

	@Override
	public List<ResidentesInformeDto> listResidentes() {
		// TODO Auto-generated method stub
		List<TiempoAcumulado> listResponse =(List<TiempoAcumulado>) tiempoAcumuladorepository.findAll();
		List<ResidentesInformeDto> residentes = new ArrayList();
		listResponse.forEach(response->{
			ResidentesInformeDto residente= new ResidentesInformeDto();
			residente.setPlaca(response.getPlaca());
			residente.setTiempo(response.getMinutos());
			residente.setCantidad(response.getTotal());
			residentes.add(residente);
		});
		return residentes;
	}
	

}
