package hr.foi.diplomski.central.controllers.records;

import hr.foi.diplomski.central.controllers.records.data.SensorData;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.service.records.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/records")
@AllArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public void saveNewDeviceRecord(@Valid @RequestBody SensorData sensorData) {
        try {
            recordService.createNewRecord(sensorData);
        } catch (BadDataException e) {
            e.printStackTrace();
        }
    }


}
