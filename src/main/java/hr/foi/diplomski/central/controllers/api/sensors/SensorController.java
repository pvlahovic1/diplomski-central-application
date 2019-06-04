package hr.foi.diplomski.central.controllers.api.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.service.sensors.SensorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@AllArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PutMapping
    @Secured("ROLE_SENSOR")
    public Sensor updateSensorData(@Valid @RequestBody SensorOutDto sensorDto) {
        return sensorService.updateSensor(sensorDto);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<List<SensorViewDto>> getAllSensors() {
        return ResponseEntity.ok(sensorService.getAllSensors());
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SensorDto> saveSensor(@Valid @RequestBody SensorDto sensorDto) {
        return ResponseEntity.ok(sensorService.saveSensor(sensorDto));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<SensorDto> getSensorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sensorService.getSensordById(id));
    }

    @GetMapping("/{id}/configuration")
    @Secured({"ROLE_ADMIN"})
    public HttpEntity<byte[]> createConfigurationForSensor(@PathVariable("id") Long id) {
        return sensorService.createCongifurationFile(id);
    }

    @GetMapping("/free")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<SensorViewDto>> getAllFreeSensors() {
        return ResponseEntity.ok(sensorService.getAllFreeSensors());
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public void deleteSensor(@PathVariable("id") Long id) {
        sensorService.deleteSensor(id);
    }

}
