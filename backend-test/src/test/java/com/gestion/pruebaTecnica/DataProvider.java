package com.gestion.pruebaTecnica;

import com.gestion.pruebaTecnica.entidades.TipoVehiculo;
import com.gestion.pruebaTecnica.entidades.Vehiculo;

import java.time.LocalDateTime;
import java.util.List;

public class DataProvider {


    public static TipoVehiculo tipoVehiculoMock(){
        System.out.println("OBJETO MOCKITO");
        return new TipoVehiculo(1L,"OFICIAL","ABC123");
    }


    public static List<Vehiculo> vehiculosListMock(){
        System.out.println("OBJETO LISTA MOCKITO");
        LocalDateTime feini = LocalDateTime.now();
        TipoVehiculo tipVe = new TipoVehiculo();
        tipVe.setNumeroPlaca("ABCD123");

        return List.of(
                new Vehiculo(1L,"ABC1234",feini,feini,"OFICIAL",0.0,0,tipVe),
                new Vehiculo(2L,"ABC5678",feini,feini,"RESIDENTE",0.0,0,tipVe)

        );
    }
}