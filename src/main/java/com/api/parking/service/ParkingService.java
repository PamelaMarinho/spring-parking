package com.api.parking.service;

import com.api.parking.model.ParkingModel;
import com.api.parking.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}


/*
service camada de comunicação entre o controller e o repository. Ele aciona o repository em determinados casos
exe comunicação como db.
Quando for necessário acessar o db o controller aciona o service e o service aciona o repository'( injeção de
dependencia)

autowired é ponto de injeção de dependencia que avisa o spring que em determinados momentos vai ter que injetar
uma dependência de ParkingRepository aqui em ParkingService

usar Transactional quando ha metodos construtivos ou destrutivos, relacionamento, deleçao ou salvamento em cascata pois
se algo der errado ela garante que tudo volte ao normal e nao tenha dados quebrados.
 */
