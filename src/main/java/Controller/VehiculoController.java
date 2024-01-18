package Controller;

import models.Vehiculo;
import models.VehiculoOficial;
import models.VehiculoResidente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.VehiculoNoResidenteService;
import service.VehiculoOficialService;
import service.VehiculoResidenteService;
import service.VehiculoService;

@RestController
public class VehiculoController {


    @Autowired
    VehiculoService vehiculoService;

    @Autowired
    VehiculoOficialService oficialService;

    @Autowired
    VehiculoResidenteService residenteService;

    @Autowired
    VehiculoNoResidenteService noResidenteService;

    @PostMapping("/altaOficial/")
    public String addOficial(
            @RequestBody String oficialPlaca){
        return oficialService.saveOficial(oficialPlaca);
    }

    @PostMapping("/altaResidente/")
    public String addResidente(
            @RequestBody String residentePlaca){
        return residenteService.saveResidente(residentePlaca);
    }

    @PostMapping("/altaNoResidente/")
    public String addNoResidente(
            @RequestBody String noResidentePlaca){
        return noResidenteService.saveNoResidente(noResidentePlaca);
    }

    @PostMapping("/altaVehiculo/")
    public Vehiculo addVehiculo(
            @RequestBody Vehiculo vehiculo){
        return vehiculoService.saveVehiculo(vehiculo);
    }

    @PostMapping("/registroEntrada/")
    public String newRegistro(
            @RequestParam(value = "placa" ) String placa){
        return vehiculoService.registroEntrada(placa);
    }

    @PostMapping("/registroSalida/")
    public String newRegistroSalida(
            @RequestParam(value = "placa" ) String placa){
        return vehiculoService.registroSalida(placa);
    }

    @DeleteMapping("/comenzarMes")
    public String comienzaMes(){
        return vehiculoService.comenzarMes();
    }


}
