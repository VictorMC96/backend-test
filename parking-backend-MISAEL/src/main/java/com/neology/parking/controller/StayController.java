package com.neology.parking.controller;

import com.neology.parking.model.entity.Stay;
import com.neology.parking.service.StayService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/stay")
public class StayController {
    private final StayService stayService;

    public StayController(StayService stayService) {
        this.stayService = stayService;
    }

    @PostMapping("/entry")
    public Stay entry(@RequestParam String plate) {
        return stayService.registerEntry(plate);
    }

    @PostMapping("/exit")
    public Optional<Double> exit(@RequestParam String plate) {
        return stayService.registerExit(plate);
    }

    @DeleteMapping("/clear")
    public void clear() {
        stayService.clearStays();
    }
}
