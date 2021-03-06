package hr.foi.diplomski.central.controllers.api.records;

import hr.foi.diplomski.central.controllers.api.records.data.SensorData;
import hr.foi.diplomski.central.service.records.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Secured("ROLE_SENSOR")
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public void saveNewDeviceRecord(@Valid @RequestBody SensorData sensorData) {
        recordService.createNewRecord(sensorData);
    }

}
