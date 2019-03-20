package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.sensors.data.SensorDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.model.Sensor;

public interface SensorService {

    Sensor updateSensor(SensorDto sensorDto) throws BadDataException;

}
