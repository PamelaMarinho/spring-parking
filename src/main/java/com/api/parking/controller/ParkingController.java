package com.api.parking.controller;

import com.api.parking.controller.dtos.ParkingDto;
import com.api.parking.model.ParkingModel;
import com.api.parking.service.ParkingService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking")
public class ParkingController {
    final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParking(@RequestBody @Valid ParkingDto parkingDto){
        if(parkingService.existsByLicenseCar(parkingDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if(parkingService.existsByParkingSpotNumber(parkingDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Spot number is already in use!");
        }
        if(parkingService.existsByApartmentAndBlock(parkingDto.getApartment(), parkingDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Apartment or block is already in use!");
        }

        var parkingModel = new ParkingModel();
        BeanUtils.copyProperties(parkingDto, parkingModel);
        parkingModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.save(parkingModel));
    }

    @GetMapping
    public ResponseEntity<List<ParkingModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingModel> parkingModelOptional = parkingService.findById(id);
        if(!parkingModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(parkingModelOptional.get());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingModel> parkingModelOptional = parkingService.findById(id);
        if(!parkingModelOptional.isPresent()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
        }
        parkingService.delete(parkingModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking spot was deleted successfully!");

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingDto parkingDto) {
        Optional<ParkingModel> parkingModelOptional = parkingService.findById(id);
        if (!parkingModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
        }
        var parkingSpotModel = new ParkingModel();
        BeanUtils.copyProperties(parkingDto, parkingSpotModel);
        parkingSpotModel.setId(parkingModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.save(parkingSpotModel));
    }


    /* outra alternativa para o m??todo put */
/*    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingDto parkingDto){
        Optional<ParkingModel> parkingModelOptional = parkingService.findById(id);
        if(!parkingModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
        }
        var parkingSpotModel = parkingModelOptional.get();
        parkingSpotModel.setParkingSpotNumber(parkingDto.getParkingSpotNumber());
        parkingSpotModel.setLicensePlateCar(parkingDto.getLicensePlateCar());
        parkingSpotModel.setModelCar(parkingDto.getModelCar());
        parkingSpotModel.setBrandCar(parkingDto.getBrandCar());
        parkingSpotModel.setColorCar(parkingDto.getColorCar());
        parkingSpotModel.setResponsibleName(parkingDto.getResponsibleName());
        parkingSpotModel.setApartment(parkingDto.getApartment());
        parkingSpotModel.setBlock(parkingDto.getBlock());
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.save(parkingSpotModel));

    }*/


}


/*
no m??todo post nao foi definida a uri pois ja foi definida a n??vel de classe e quando receber o m??todo post o m??todo
saveParking sera acionado.

ResponseEntity<Object> para montar uma resposta(corpo e status). Object - pois ?? feito diferentes tipos de retorno..
vai receber o dto com json

nas versoes anteriores jdk8 do java era necess??rio declarar o tipo: ParkingModel parkingModel = new ParkingModel()
a partir da versao 8 ?? poss??vel declarar com var

ResponseBody - receber os dados json.

Valid - para que as valida????es do dto seja realizadas quando receber esses dados, n??o basta adicionar as nnotations.
exem: se o cliente n??o enviou algum dado quando chegar aqui o @valid nao deixa entrar no m??todo e envia um bad request

a persistencia de dados deve ser feita no formato model e recebemos como json. Por esse motivo usamos o BeanUtils.copyPropertis
(porem ha outras maneiras de converter) passando o que ser?? convertido e no segundo parametro o formato que deve ser convertido.

como a data nao sera enviada pelo cliente pois sera gerada automaticamente ?? usado parkingModel.setRegistrationDate

@PathVariable: pode ser usada para manipular vari??veis de modelo no mapeamento de URI de solicita????o e
defini-las como par??metros de m??todo. Colocar o id da uri em chaves pq vai obte-lo

como o tipo do parkingModelOptional ?? optional ?? usado o get() quando ?? passado no body para obter o model
*/