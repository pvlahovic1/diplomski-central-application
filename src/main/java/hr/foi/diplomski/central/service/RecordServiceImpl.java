package hr.foi.diplomski.central.service;

import hr.foi.diplomski.central.controllers.data.SensorData;
import hr.foi.diplomski.central.controllers.data.SensorRecord;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.model.record.RecordId;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final SensorRepository sensorRepository;
    private final BeaconRepository beaconRepository;

    @Override
    public void createNewRecord(SensorData sensorData) throws BadDataException {
        var sensorOptional = sensorRepository.findBySensorId(sensorData.getDeviceId());

        if (sensorOptional.isPresent()) {
            saveRecordFromSensor(sensorOptional.get(), sensorData.getSensorRecords());
        } else {
            throw new BadDataException("Sensor identify data is not recognized!");
        }
    }

    private void saveRecordFromSensor(Sensor sensor, List<SensorRecord> sensorRecords) {
        for (SensorRecord sensorRecord : sensorRecords) {
            var beaconOptional = beaconRepository.findByUuidAndMajorAndMinor(sensorRecord.getUuid(), sensorRecord.getMajor(),
                    sensorRecord.getMinor());

            if (beaconOptional.isPresent()) {
                Beacon beacon = beaconOptional.get();

                var newestBeaconRecordOptional = recordRepository
                        .findFirstByRecordId_BeaconAndRecordId_SensorOrderByRecordId_RecordDateDesc(beacon, sensor);

                if (newestBeaconRecordOptional.isPresent()) {
                    Record newestBeaconRecord = newestBeaconRecordOptional.get();

                    if (isDataImportant(newestBeaconRecord.getDistance(), sensorRecord.getDistance(), 1)) {
                        saveNewRecordData(sensorRecord, sensor, beacon);
                        log.info("Saving new record: {} for sensor: {}", sensorRecord, sensor.getSensorName());
                    } else {
                        log.info("There will be no saving for record: {} because the distance did not change very much.", sensorRecord);
                    }
                } else {
                    saveNewRecordData(sensorRecord, sensor, beacon);
                }
            } else {
                log.info("Beacon in record data is not recognized: {}", sensorRecord);
            }
        }
    }

    private boolean isDataImportant(double lastKnownDistance, double newDistance, double threshold) {
        double absDiff = Math.abs(newDistance - lastKnownDistance);

        return absDiff > threshold;
    }

    private void saveNewRecordData(SensorRecord sensorRecord, Sensor sensor, Beacon beacon) {
        RecordId recordId = new RecordId(sensor, beacon, LocalDateTime.now());
        recordRepository.save(new Record(recordId, sensorRecord.getDistance()));
    }
}
