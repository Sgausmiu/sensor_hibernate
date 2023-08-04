package ru.tech.sensor.device.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.tech.sensor.device.dto.MeasurementDTO;
import ru.tech.sensor.device.exception.MeasurementNotAddedException;
import ru.tech.sensor.device.models.Measurement;
import ru.tech.sensor.device.service.MeasurementService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new MeasurementNotAddedException(errorMsg.toString());

        }

        measurementService.save(convertToAddMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //по совету коллег - предложили передавать не список, а один объект json
    @GetMapping
    public List<MeasurementDTO> getAllMeasurement(){
        return measurementService.getAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDaysCount(){

        return  measurementService.getAll().stream().filter(Measurement::isRaining).count();
    }

    public Measurement convertToAddMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);}

    public MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(measurement.getValue());
        measurementDTO.setRaining(measurement.isRaining());
        measurementDTO.setSensor(measurement.getSensor());
        measurementDTO.setLocalResolutionTime(measurement.getLocalResolutionTime());


        return measurementDTO;
       // return modelMapper.map(measurement, MeasurementDTO.class); с ModelMapper не возвращает значения, причину не искал.
    }

}
