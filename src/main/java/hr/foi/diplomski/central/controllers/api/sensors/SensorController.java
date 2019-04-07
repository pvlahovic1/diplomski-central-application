package hr.foi.diplomski.central.controllers.api.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.service.sensors.SensorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@AllArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PutMapping
    public Sensor updateSensorData(@Valid @RequestBody SensorOutDto sensorDto) {
        try {
            return sensorService.updateSensor(sensorDto);
        } catch (BadDataException e) {
            e.printStackTrace();
        }
        //TODO
        return null;
    }

    @GetMapping("/free")
    public ResponseEntity<List<SensorViewDto>> getAllFreeSensors() {
        return ResponseEntity.ok(sensorService.getAllFreeSensors());
    }

}
