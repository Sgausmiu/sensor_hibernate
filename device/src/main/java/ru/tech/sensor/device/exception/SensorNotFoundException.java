package ru.tech.sensor.device.exception;

public class SensorNotFoundException extends RuntimeException{
    public SensorNotFoundException(String msg){
        super(msg);
    }
}
