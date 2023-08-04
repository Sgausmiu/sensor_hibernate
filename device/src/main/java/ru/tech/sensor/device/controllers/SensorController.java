package ru.tech.sensor.device.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.tech.sensor.device.dto.SensorDTO;
import ru.tech.sensor.device.exception.MeasurementErrorResponse;
import ru.tech.sensor.device.exception.MeasurementNotAddedException;
import ru.tech.sensor.device.models.Sensor;
import ru.tech.sensor.device.service.SensorService;
import ru.tech.sensor.device.exception.SensorNotFoundException;
import ru.tech.sensor.device.utils.SensorValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> addSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                BindingResult bindingResult) {

        Sensor sensorAdd = convertToAdd(sensorDTO);
        sensorValidator.validate(sensorAdd, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotFoundException(errorMsg.toString());
        }

        sensorService.save(sensorAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<SensorDTO> getAllSensors() {
        return sensorService.getAll().stream().map(this::convertToSensorDTO)
                .collect(Collectors.toList());
    }

    private Sensor convertToAdd(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    public SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotAddedException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
