package ru.tech.sensor.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.sensor.device.models.Sensor;
import ru.tech.sensor.device.repositories.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;


    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void save(Sensor sensor){
        sensorRepository.save(sensor);
    }

    public Optional<Sensor> searchByName(String name){
        return sensorRepository.searchByName(name);
    }

    public List<Sensor> getAll() {return sensorRepository.findAll();}

}
