package mx.ms.parking.mutations;

import mx.ms.parking.entity.Acceso;
import mx.ms.parking.entity.Vehiculo;
import mx.ms.parking.model.RequestAcceso;
import mx.ms.parking.services.IAccesoServices;
import mx.ms.parking.services.IVehiculoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
@Service
public class Accesos {
    @Autowired
    private IAccesoServices accesoServices;
    @Autowired
    private IVehiculoServices vehiculoServices;

    public Accesos registrarAcceso(RequestAcceso acceso){
        Calendar fechaActual; // para almacenar una fecha
        fechaActual = Calendar.getInstance(); // obtiene la fecha actual
        //Se busca vehiculo con numero de placas
        Vehiculo vehiculo= vehiculoServices.findByPlacas(acceso.getPlaca());
        if(acceso.getAcceso()==false){

            //Al validar que no existe registro previo con estas placas es nuevo carro sin alta previa como oficial o residente por lo tanto se genera el registro de entrada
            if(vehiculo.getId()==null ){
                vehiculo.setIdTipoVehiculo(3L);
                vehiculo.setPlaca(acceso.getPlaca());
                //se da de alta unidad como tipo no residente
                Vehiculo vehiculoNew=vehiculoServices.saveAndFlush(vehiculo);
                if(vehiculoNew.getId()!=null){
                    //si se genera el alta, se genera el registro de entrada
                    Acceso ac=new Acceso();
                    ac.setIdVehiculo(vehiculoNew.getId());
                    this.registrarEntrada(ac);
                }
            }else {
                //existe unidad entonces validamos si tiene acceso del dia actual
                Acceso ac=accesoServices.findByIdVehiculoAndFhEntrada(vehiculo.getIdTipoVehiculo(),fechaActual);
                //Si existe actualizamos registro de salida
                if(ac.getId()!=null){
                    this.registrarSalida(ac,vehiculo);
                }else{
                    //No existe registro previo entonces generamos entrada
                    ac.setIdVehiculo(vehiculo.getIdTipoVehiculo());
                    this.registrarEntrada(ac);
                }
            }

        }
        if(acceso.getAcceso()==true){
            if(vehiculo.getId()!=null ) {
                //existe unidad entonces validamos si tiene acceso del dia actual
                Acceso ac2=accesoServices.findByIdVehiculoAndFhEntrada(vehiculo.getIdTipoVehiculo(),fechaActual);
                //Si existe actualizamos registro de salida
                if(ac2.getId()!=null){
                    this.registrarSalida(ac2,vehiculo);
                }
            }

        }


        return null;
    }

    public Acceso registrarEntrada(Acceso acceso){
        Calendar fechaActual; // para almacenar una fecha
        fechaActual = Calendar.getInstance(); // obtiene la fecha actual
        acceso.setFhEntrada(fechaActual);
        acceso.setIdVehiculo(acceso.getIdVehiculo());

        return accesoServices.saveAndFlush(acceso) ;
    }
    public Acceso registrarSalida(Acceso acceso,Vehiculo vehiculo){
        Calendar fechaActual; // para almacenar una fecha
        fechaActual = Calendar.getInstance(); // obtiene la fecha actual
        acceso.setFhSalida(fechaActual);
        int  minDia=this.difEnMinutos(acceso.getFhEntrada(),fechaActual);
        switch (vehiculo.getIdTipoVehiculo().intValue()) {
            //tipo oficial
            case 1:

                acceso.setTiempoTotal(minDia);
                return accesoServices.saveAndFlush(acceso);
            case 2:
            //tipo residente
                int totalAcumulado=minDia+ vehiculo.getTiempoTotalAcumulado();
                vehiculo.setTiempoTotalAcumulado(totalAcumulado);
                vehiculoServices.saveAndFlush(vehiculo);
                return accesoServices.saveAndFlush(acceso);
            case 3:
            //No residente
                Double importeMinuto=2.00;
                Double total=minDia*importeMinuto;
                acceso.setImporte(total);
                acceso.setTiempoTotal(minDia);
                return accesoServices.saveAndFlush(acceso);
            default:
                break;
        }
        return null;
    }

public void eliminarRegistrosOficial(){
        this.accesoServices.eliminarAccesosOficiales();
}
    private static int difEnMinutos(Calendar fhInicial, Calendar fhFinal) {
        if (fhInicial == null || fhFinal == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }

        // Obtener la diferencia en milisegundos
        long diffMillis = fhFinal.getTimeInMillis() - fhInicial.getTimeInMillis();

        // Convertir a minutos
        int diffMinutos = (int) (diffMillis / (1000 * 60));

        return diffMinutos;
    }



}
