package service;

import models.VehiculoOficial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.VehiculoOficialRepository;

@Service
public class VehiculoOficialService {

    @Autowired
    VehiculoOficialRepository oficialRepository;

    public String saveOficial(String oficialPlaca){
        if(oficialPlaca!= null || !oficialPlaca.isEmpty()){
            VehiculoOficial vehiculoOficial = new VehiculoOficial();
            vehiculoOficial.setPlaca(oficialPlaca);
            vehiculoOficial.setTipo("Oficial");
           oficialRepository.save(vehiculoOficial);
           return "Se agregó correctamente el vehiculo oficial";

        }
        return "No se pudo agregar un vehiculo oficial";
    }


}
