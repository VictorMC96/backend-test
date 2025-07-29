package com.example.abrahamtest.Parking_management.controller;

import com.example.abrahamtest.Parking_management.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping ("/parking")

public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/menu")
    public String showMenu() {
        return "menu";
    }

    @PostMapping("/register-official")
    public String registerOfficialVehicle(@RequestParam String licensePlate, RedirectAttributes redirectAttributes) {
        parkingService.registerOfficialVehicle(licensePlate);
        redirectAttributes.addFlashAttribute("message", "Vehículo oficial registrado exitosamente");
        return "redirect:/parking/menu";
    }

    @PostMapping("/register-resident")
    public String registerResidentVehicle(@RequestParam String licensePlate, RedirectAttributes redirectAttributes) {
        parkingService.registerResidentVehicle(licensePlate);
        redirectAttributes.addFlashAttribute("message", "Vehículo residente registrado exitosamente");
        return "redirect:/parking/menu";
    }

    @PostMapping("/register-entry")
    public String registerEntry(@RequestParam String licensePlate, RedirectAttributes redirectAttributes) {
        parkingService.registerEntry(licensePlate);
        redirectAttributes.addFlashAttribute("message", "Entrada registrada exitosamente");
        return "redirect:/parking/menu";
    }

    @PostMapping("/register-exit")
    public String registerExit(@RequestParam String licensePlate, RedirectAttributes redirectAttributes) {
        parkingService.registerExit(licensePlate);
        redirectAttributes.addFlashAttribute("message", "Salida registrada exitosamente");
        return "redirect:/parking/menu";
    }

    @PostMapping("/start-month")
    public String startNewMonth(RedirectAttributes redirectAttributes) {
        parkingService.startNewMonth();
        redirectAttributes.addFlashAttribute("message", "Nuevo mes iniciado");
        return "redirect:/parking/menu";
    }



    @GetMapping("/generate-report")
    public String generateResidentPaymentReport(Model model) {
        String report = parkingService.generateResidentPaymentReport();
        model.addAttribute("report", report);
        return "report";
    }

}
