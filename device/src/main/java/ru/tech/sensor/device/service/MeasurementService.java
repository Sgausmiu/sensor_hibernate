package ru.tech.sensor.device.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.sensor.device.models.Measurement;
import ru.tech.sensor.device.models.Sensor;
import ru.tech.sensor.device.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;
    private final Optional <Sensor> sensor;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService, Sensor sensor) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.sensor = Optional.ofNullable(sensor);
    }

    @Transactional
    public void save(Measurement measurement){

        searchSensor(measurement);
        measurementRepository.save(measurement);
    }

    //укажем конкретный прибор для функции addMeasurement() и привяжем к текущему времени получение инфы с прибора
    public void searchSensor(@NotNull Measurement measurement){
//       Optional<Sensor> optionalSensor = measurement.setSensor(sensorService.searchByName(measurement.getSensor().getName()).get());
//        Sensor sensor = optionalSensor.orElseThrow(() -> new IllegalArgumentException("Не добавлен прибор для измерений."));
//        measurement.setSensor(sensor);
//        measurement.setLocalResolutionTime(LocalDateTime.now());
//        Sensor sensor = measurement.getSensor();
//        if (sensor == null) {
//            throw new IllegalArgumentException("Не добавлен прибор для измерений.");
//        }

        Optional<Sensor> optionalSensor = sensorService.searchByName(measurement.getSensor().getName());
        if (optionalSensor.isPresent()) {
            measurement.setSensor(optionalSensor.get());
            measurement.setLocalResolutionTime(LocalDateTime.now());
        } else {
            throw new IllegalArgumentException("Не добавлен прибор для измерений.");
        }
    }
    public List<Measurement> getAll(){
        return measurementRepository.findAll();
    }
}
