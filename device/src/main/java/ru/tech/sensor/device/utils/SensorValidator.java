package ru.tech.sensor.device.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tech.sensor.device.models.Sensor;
import ru.tech.sensor.device.service.SensorService;

@Component
public class SensorValidator implements Validator {

    private SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;
        if(sensorService.searchByName(sensor.getName()).isPresent())
            errors.rejectValue("name", "Sensor also is registered.");

    }
}
