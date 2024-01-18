package service;

import models.VehiculoNoResidente;
import models.VehiculoResidente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.VehiculoNoResidenteRepository;
import repository.VehiculoResidenteRepository;

@Service
public class VehiculoNoResidenteService {

    @Autowired
    VehiculoNoResidenteRepository noResidenteRepository;

    public String saveNoResidente(String residentePlaca){
        if(residentePlaca!= null || !residentePlaca.isEmpty()){
            VehiculoNoResidente vehiculoNoResidente = new VehiculoNoResidente();
            vehiculoNoResidente.setPlaca(residentePlaca);
            vehiculoNoResidente.setTipo("No Residente");
            noResidenteRepository.save(vehiculoNoResidente);
            return "Se agregó correctamente el vehiculo no residente";

        }
        return "No se pudo agregar un vehiculo no residente";
    }
}
