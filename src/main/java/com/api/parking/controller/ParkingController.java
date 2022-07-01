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


    /* outra alternativa para o método put */
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
no método post nao foi definida a uri pois ja foi definida a nível de classe e quando receber o método post o método
saveParking sera acionado.

ResponseEntity<Object> para montar uma resposta(corpo e status). Object - pois é feito diferentes tipos de retorno..
vai receber o dto com json

nas versoes anteriores jdk8 do java era necessário declarar o tipo: ParkingModel parkingModel = new ParkingModel()
a partir da versao 8 é possível declarar com var

ResponseBody - receber os dados json.

Valid - para que as validações do dto seja realizadas quando receber esses dados, não basta adicionar as nnotations.
exem: se o cliente não enviou algum dado quando chegar aqui o @valid nao deixa entrar no método e envia um bad request

a persistencia de dados deve ser feita no formato model e recebemos como json. Por esse motivo usamos o BeanUtils.copyPropertis
(porem ha outras maneiras de converter) passando o que será convertido e no segundo parametro o formato que deve ser convertido.

como a data nao sera enviada pelo cliente pois sera gerada automaticamente é usado parkingModel.setRegistrationDate

@PathVariable: pode ser usada para manipular variáveis de modelo no mapeamento de URI de solicitação e
defini-las como parâmetros de método. Colocar o id da uri em chaves pq vai obte-lo

como o tipo do parkingModelOptional é optional é usado o get() quando é passado no body para obter o model
*/