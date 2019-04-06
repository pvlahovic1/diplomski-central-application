package hr.foi.diplomski.central.controllers.api.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.service.sensors.SensorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/sensors")
@AllArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PutMapping
    public Sensor updateSensorData(@Valid @RequestBody SensorDto sensorDto) {
        try {
            return sensorService.updateSensor(sensorDto);
        } catch (BadDataException e) {
            e.printStackTrace();
        }
        //TODO
        return null;
    }

}
