package com.api.parking.service;

import com.api.parking.model.ParkingModel;
import com.api.parking.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingService {

    final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }
    @Transactional
    public ParkingModel save(ParkingModel parkingModel) {
        return parkingRepository.save(parkingModel);
    }

    public boolean existsByLicenseCar(String licensePlateCar) {
        return parkingRepository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingModel> findAll() {
        return parkingRepository.findAll();
    }

    public Optional<ParkingModel> findById(UUID id) {
        return parkingRepository.findById(id);
    }
    @Transactional
    public void delete(ParkingModel parkingModel) {
        parkingRepository.delete(parkingModel);
    }
}


/*
service camada de comunicação entre o controller e o repository. Ele aciona o repository em determinados casos
exe comunicação como db.
Quando for necessário acessar o db o controller aciona o service e o service aciona o repository'( injeção de
dependencia)

autowired é ponto de injeção de dependencia que avisa o spring que em determinados momentos vai ter que injetar
uma dependência de ParkingRepository aqui em ParkingService

usar Transactional quando ha metodos construtivos ou destrutivos, relacionamento, deleçao ou salvamento em cascata pois
se algo der errado ela garante hollback que tudo volte ao normal e nao tenha dados quebrados.
 */
