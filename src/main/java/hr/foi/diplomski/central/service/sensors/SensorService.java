package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.model.Sensor;

import java.util.List;

public interface SensorService {

    Sensor updateSensor(SensorOutDto sensorOutDto) throws BadDataException;

    List<SensorViewDto> getAllSensorsViewByRoom(Long roomId);

    List<SensorViewDto> getAllFreeSensors();

}
