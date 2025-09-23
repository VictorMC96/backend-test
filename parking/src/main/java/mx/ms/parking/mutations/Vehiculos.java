package mx.ms.parking.mutations;

import mx.ms.parking.entity.Vehiculo;
import mx.ms.parking.model.RequestVehiculo;
import mx.ms.parking.services.IVehiculoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Vehiculos {
    @Autowired
    private IVehiculoServices vehiculoServices;

    public Vehiculo registrarVehiculo(RequestVehiculo vehiculo){

        Vehiculo ve=new Vehiculo();
        ve.setPlaca(vehiculo.getPlacas());
        ve.setIdTipoVehiculo(vehiculo.getTipoVehiculo());
        return vehiculoServices.saveAndFlush(ve);
    }
    public void resetTiempoResidentes(){
        this.vehiculoServices.resetTiempoResidentes();
    }

}
