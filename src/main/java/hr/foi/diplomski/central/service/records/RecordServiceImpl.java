package hr.foi.diplomski.central.service.records;

import hr.foi.diplomski.central.controllers.api.records.data.SensorData;
import hr.foi.diplomski.central.controllers.api.records.data.SensorRecord;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Room;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.model.record.RecordId;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.repository.SensorRepository;
import hr.foi.diplomski.central.service.socket.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final SensorRepository sensorRepository;
    private final BeaconRepository beaconRepository;
    private final WebSocketService websocketService;

    @Override
    public void createNewRecord(SensorData sensorData) {
        Sensor sensor = sensorRepository.findBySensorId(sensorData.getDeviceId())
                .orElseThrow(() -> new BadRequestException("Sensor identify data is not recognized!"));

        saveRecordsFromSensor(sensor, sensorData.getSensorRecords());

        if (sensor.getRoom() != null) {
            websocketService.refreshRoomsState(Collections.singletonList(sensor.getRoom().getId()));
        }
    }

    private void saveRecordsFromSensor(Sensor sensor, List<SensorRecord> sensorRecords) {
        for (SensorRecord sensorRecord : sensorRecords) {
            var beaconOptional = beaconRepository.findByUuidAndMajorAndMinor(sensorRecord.getUuid(),
                    sensorRecord.getMajor(), sensorRecord.getMinor());

            if (beaconOptional.isPresent()) {
                Beacon beacon = beaconOptional.get();

                var lastKnownBeaconRecordOptional = recordRepository
                        .findFirstByRecordId_BeaconOrderByRecordId_RecordDateDesc(beacon);

                if (lastKnownBeaconRecordOptional.isPresent()) {
                    Record lastKnowBeaconRecord = lastKnownBeaconRecordOptional.get();

                    if (isRecordInsideRoom(sensorRecord, sensor.getRoom())) {
                        if (isDataImportant(lastKnowBeaconRecord, sensor, sensorRecord, 1)) {
                            saveNewRecordData(sensorRecord, sensor, beacon);
                            log.info("Saving new record: {} for sensor: {}", sensorRecord, sensor.getSensorName());
                        } else {
                            log.info("There will be update for: {} because beacon is still in range for this sensor.", sensorRecord);
                            recordRepository.delete(lastKnowBeaconRecord);
                            saveNewRecordData(sensorRecord, sensor, beacon);
                        }
                    } else {
                        log.info("Record wont be saved because beacon record is not in room");
                    }
                } else {
                    saveNewRecordData(sensorRecord, sensor, beacon);
                }
            } else {
                log.info("Beacon in record data is not recognized: {}", sensorRecord);
            }
        }
    }

    private boolean isRecordInsideRoom(SensorRecord sensorRecord, Room room) {
        if (room == null) {
            return true;
        }

        boolean status = false;
        double maxDistance = room.calculateMaxDistance();
        if (maxDistance >= sensorRecord.getDistance()) {
            status = true;
        } else {
            log.info("Max distance for this room is: {} and actual record is: {}", maxDistance,
                    sensorRecord.getDistance());
        }

        return status;
    }

    private boolean isDataImportant(Record lastKnowBeaconRecord, Sensor currentSensor,
                                    SensorRecord currentSensorRecord, double threshold) {
        if (lastKnowBeaconRecord.getRecordId().getSensor().getId().equals(currentSensor.getId())) {
            double absDiff = Math.abs(currentSensorRecord.getDistance() - lastKnowBeaconRecord.getDistance());
            return absDiff > threshold;
        } else {
            return currentSensorRecord.getDistance() < lastKnowBeaconRecord.getDistance();
        }
    }

    private void saveNewRecordData(SensorRecord sensorRecord, Sensor sensor, Beacon beacon) {
        RecordId recordId = new RecordId(sensor, beacon, LocalDateTime.now());
        recordRepository.save(new Record(recordId, sensorRecord.getDistance()));
    }
}
