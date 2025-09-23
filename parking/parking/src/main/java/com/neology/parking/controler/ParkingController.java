package com.neology.parking.controler;


import com.neology.parking.service.ParkingService;
import com.neology.parking.service.ParkingService.ExitResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ParkingController {
	private final ParkingService service;
    public ParkingController(ParkingService service) { this.service = service; }

    @PostMapping("/vehicles/official")
    public ResponseEntity<?> addOfficial(@RequestBody Map<String,String> body) {
        String plate = body.get("plate");
        
    	System.out.println("plate: " + plate);

        service.addOfficial(plate);
        return ResponseEntity.ok(Map.of("status","OK","plate",plate));
    }

    @PostMapping("/vehicles/resident")
    public ResponseEntity<?> addResident(@RequestBody Map<String,String> body) {
        String plate = body.get("plate");
        service.addResident(plate);
        return ResponseEntity.ok(Map.of("status","OK","plate",plate));
    }

    @PostMapping("/entry")
    public ResponseEntity<?> registerEntry(@RequestBody Map<String,String> body) {
        String plate = body.get("plate");
        service.registerEntry(plate);
        return ResponseEntity.ok(Map.of("status","entry recorded","plate",plate));
    }

    @PostMapping("/exit")
    public ResponseEntity<?> registerExit(@RequestBody Map<String,String> body) {
        String plate = body.get("plate");
        ExitResult res = service.registerExit(plate);
        return ResponseEntity.ok(Map.of(
            "plate", res.plate,
            "type", res.type,
            "minutes", res.minutes,
            "amount", res.amount
        ));
    }

    @PostMapping("/month/start")
    public ResponseEntity<?> beginMonth() {
        service.beginMonth();
        return ResponseEntity.ok(Map.of("status","month started"));
    }

    @GetMapping("/reports/residents")
    public ResponseEntity<?> generateResidentsReport(@RequestParam String filename) {
        String path = service.generateResidentsReport(filename);
        return ResponseEntity.ok(Map.of("file", path));
    }
    
}
