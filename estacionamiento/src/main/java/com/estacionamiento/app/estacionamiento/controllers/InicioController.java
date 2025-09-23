package com.estacionamiento.app.estacionamiento.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class InicioController {

    @GetMapping("test")
    public String index(){
        return "Admon estacionamiento";
    }

}
