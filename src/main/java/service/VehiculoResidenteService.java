package service;

import models.VehiculoResidente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.VehiculoResidenteRepository;

@Service
public class VehiculoResidenteService {

    @Autowired
    VehiculoResidenteRepository residenteRepository;

    public String saveResidente(String residentePlaca){
        if(residentePlaca!= null || !residentePlaca.isEmpty()){
            VehiculoResidente vehiculoOficial = new VehiculoResidente();
            vehiculoOficial.setPlaca(residentePlaca);
            vehiculoOficial.setTipo("Residente");
            residenteRepository.save(vehiculoOficial);
            return "Se agregó correctamente el vehiculo residente";

        }
        return "No se pudo agregar un vehiculo residente";
    }
}
