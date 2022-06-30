package com.api.parking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking")
public class ParkingController {
    final ParkingController parkingController;

    public ParkingController(ParkingController parkingController) {
        this.parkingController = parkingController;
    }

    @PostMapping
    public ResponseEntity<Object> saveParking()


}


/*
no método post nao foi definida a uri pois ja foi definida a nível de classe e quando receber o método post o método
saveParking sera acionado.

ResponseEntity<Object> pois é feito diferentes tipos de retorno.. vai receber o dto com json
*/