package com.alwx.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alwx.backend.models.Vehicle;


/**
 * Репозиторий для работы с автомобилями.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>  {
    Optional<Vehicle> findByName(String name);
    List<Vehicle> findByCoordinatesId(Long coordinatesId);
    List<Vehicle> findAllByCoordinatesId(Long coordinatesId);
    Boolean existsByName(String name);
}
