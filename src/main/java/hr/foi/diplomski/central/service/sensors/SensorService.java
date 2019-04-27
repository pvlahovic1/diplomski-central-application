package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.model.Sensor;
import org.springframework.http.HttpEntity;

import java.util.List;

public interface SensorService {

    Sensor updateSensor(SensorOutDto sensorOutDto);

    List<SensorViewDto> getAllSensorsViewByRoom(Long roomId);

    List<SensorViewDto> getAllFreeSensors();

    List<SensorViewDto> getAllSensors();

    SensorDto getSensordById(Long id);

    SensorDto saveSensor(SensorDto sensorDto);

    void deleteSensor(Long senzorId);

    HttpEntity<byte[]> createCongifurationFile(Long sensorId);

    void updateSensorLasPresentTime(Sensor sensor);

}
