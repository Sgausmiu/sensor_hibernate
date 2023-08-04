package ru.tech.sensor.device.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tech.sensor.device.models.Sensor;

import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String> {
    Optional<Sensor> searchByName(String name);


}
