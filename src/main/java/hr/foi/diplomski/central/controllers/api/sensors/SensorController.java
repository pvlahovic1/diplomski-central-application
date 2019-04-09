package hr.foi.diplomski.central.controllers.api.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
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

    @GetMapping
    public ResponseEntity<List<SensorViewDto>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }

    @PostMapping
    public ResponseEntity<SensorDto> saveSensor(@Valid @RequestBody SensorDto sensorDto) {
        return ResponseEntity.ok(sensorService.saveSensor(sensorDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDto> getSensorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sensorService.getSensordById(id));
    }

    @GetMapping("/free")
    public ResponseEntity<List<SensorViewDto>> getAllFreeSensors() {
        return ResponseEntity.ok(sensorService.getAllFreeSensors());
    }

    @DeleteMapping("/{id}")
    public void deleteSensor(@PathVariable("id") Long id) {
        sensorService.deleteSensor(id);
    }

}
