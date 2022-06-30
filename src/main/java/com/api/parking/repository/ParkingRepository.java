package com.api.parking.repository;

import com.api.parking.model.ParkingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository                                             //model e o tipo
public interface ParkingRepository extends JpaRepository<ParkingModel, UUID> {
}
