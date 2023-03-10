package com.parkingservice.project.controller;

import com.parkingservice.project.dto.ParkingLotRequest;
import com.parkingservice.project.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/onboard")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @PostMapping(path = "/new", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewParkingLot(@RequestBody ParkingLotRequest parkingLotRequest) {
        parkingLotService.addNewParkingLot(parkingLotRequest);
        return ResponseEntity.ok("Successfully onboarded new Parking Lot.");
    }

}
