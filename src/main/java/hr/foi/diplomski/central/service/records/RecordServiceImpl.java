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
import hr.foi.diplomski.central.service.centralaudit.CentralAuditService;
import hr.foi.diplomski.central.service.socket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static hr.foi.diplomski.central.model.CentralAudit.ROOM_ENTER_MESSAGE;
import static hr.foi.diplomski.central.model.CentralAudit.ROOM_EXIT_MESSAGE;
import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final static String LOG_MESSAGE_SAME_ROOM = "Novi zapis se nalazi u istoj prostoji kao i zadnji zapis.";
    private final static String LOG_MESSAGE_DIFF_ROOM = "Novi zapis se ne nalazi u istoj prostoji kao i zadnji zapis.";

    @Value("${beacon.record.distance.threshold}")
    private Long threshold;
    @Value("${beacon.record.duration}")
    private Long maxRecordDuration;

    private final RecordRepository recordRepository;
    private final SensorRepository sensorRepository;
    private final BeaconRepository beaconRepository;
    private final WebSocketService websocketService;
    private final CentralAuditService centralAuditService;

    @Override
    public void createNewRecord(SensorData sensorData) {
        Sensor sensor = sensorRepository.findBySensorId(sensorData.getDeviceId())
                .orElseThrow(() -> new BadRequestException("Sensor identify data is not recognized!"));

        if (sensor.getRoom() != null) {
            processSensorData(sensor, sensorData.getSensorRecords());
            websocketService.refreshRoomsState(Collections.singletonList(sensor.getRoom().getId()));
        } else {
            log.info(String.format("Senzor %s nema postavljenu prostoriju te se zapis neće zapisati",
                    sensor.getSensorName()));
        }
    }

    private void processSensorData(Sensor sensor, List<SensorRecord> sensorRecords) {
        for (SensorRecord sensorRecord : sensorRecords) {
            Beacon beacon = beaconRepository.findByUuidAndMajorAndMinor(sensorRecord.getUuid(),
                    sensorRecord.getMajor(), sensorRecord.getMinor()).orElseThrow(() -> new BadRequestException(
                    String.format("Beacon in record data is not recognized: %s", sensorRecord)));

            if (isRecordInsideRoom(sensorRecord, sensor.getRoom())) {
                var lastKnownBeaconRecordOptional = recordRepository
                        .findFirstByRecordId_BeaconOrderByRecordId_RecordDateDesc(beacon);

                if (lastKnownBeaconRecordOptional.isPresent()) {
                    Record lastKnownBeaconRecord = lastKnownBeaconRecordOptional.get();

                    if (lastKnownBeaconRecord.getRecordId().getSensor().getRoom() != null) {
                        if (sensor.getRoom().equals(lastKnownBeaconRecord.getRecordId().getSensor().getRoom())) {
                            if (isRecordDistanceGreaterThanThreshold(lastKnownBeaconRecord.getDistance(),
                                    sensorRecord.getDistance(), threshold)) {
                                log.info(String.format("%s Razlika udaljenosti novog zapisa i postojećeg zapisa je " +
                                        "veća od %s. Ažuriram", LOG_MESSAGE_SAME_ROOM, threshold));
                                updateSensorRecord(lastKnownBeaconRecord, sensorRecord, sensor, beacon);
                            } else {
                                log.info(String.format("%s Razlika udaljenosti novog zapisa i postojećeg zapisa je " +
                                        "manja od %s M. Ažuriram vrijeme zapisa", LOG_MESSAGE_SAME_ROOM, threshold));
                                updateSensorRecordTime(lastKnownBeaconRecord);
                            }
                        } else {
                            if (isMaxRecordDurationExceeded(lastKnownBeaconRecord.getRecordId().getRecordDate())) {
                                log.info(String.format("%s Zadnji zapis nije validan zbog vremena. Ažuriram",
                                        LOG_MESSAGE_DIFF_ROOM));
                                updateSensorRecord(lastKnownBeaconRecord, sensorRecord, sensor, beacon);
                                centralAuditService.saveAudit(1L, sensor, beacon, ROOM_ENTER_MESSAGE);
                            } else {
                                if (sensorRecord.getDistance() <= lastKnownBeaconRecord.getDistance()) {
                                    log.info(String.format("%s Udaljenost novog zapisa je manja. Ažuriram",
                                            LOG_MESSAGE_DIFF_ROOM));
                                    updateSensorRecord(lastKnownBeaconRecord, sensorRecord, sensor, beacon);
                                    centralAuditService.saveAudit(1L, sensor, beacon, ROOM_ENTER_MESSAGE);
                                } else {
                                    log.info(String.format("%s Udaljenost novog zapisa je veća. Ne ažuriram",
                                            LOG_MESSAGE_DIFF_ROOM));
                                }
                            }
                        }
                    } else {
                        log.info("Senzor zadnjeg zapisa nema postavljenu prostoriju. Ažuriram s novim vrijednostima");
                        updateSensorRecord(lastKnownBeaconRecord, sensorRecord, sensor, beacon);
                        centralAuditService.saveAudit(1L, sensor, beacon, ROOM_ENTER_MESSAGE);
                    }
                } else {
                    log.info(String.format("Unutar baze ne postoji zapis za beacon %s te se on zapisuje", beacon));
                    saveNewRecordData(sensor, beacon, sensorRecord.getDistance());
                    centralAuditService.saveAudit(1L, sensor, beacon, ROOM_ENTER_MESSAGE);
                }
            } else {
                log.info(String.format("Zapis %s se ne nalazi unutar prostorije %s jer je udaljenost veća od maksimalne udaljenosti prostorije %s",
                        sensorRecord, sensor.getRoom().getRoomName(), sensor.getRoom().calculateMaxDistance()));
                centralAuditService.saveAudit(1L, sensor, beacon, ROOM_EXIT_MESSAGE);
            }
        }
    }

    private boolean isMaxRecordDurationExceeded(LocalDateTime lastKnownBeaconRecordTime) {
        return MINUTES.between(lastKnownBeaconRecordTime, LocalDateTime.now()) >= maxRecordDuration;
    }

    private boolean isRecordInsideRoom(SensorRecord sensorRecord, Room room) {
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

    private boolean isRecordDistanceGreaterThanThreshold(double lastKnowDistance, double currentDistance, double threshold) {
        double absDiff = Math.abs(currentDistance - lastKnowDistance);
        return absDiff > threshold;
    }

    private void updateSensorRecordTime(Record lastKnowBeaconRecord) {
        Sensor sensor = lastKnowBeaconRecord.getRecordId().getSensor();
        Beacon beacon = lastKnowBeaconRecord.getRecordId().getBeacon();
        double distance = lastKnowBeaconRecord.getDistance();

        recordRepository.delete(lastKnowBeaconRecord);
        saveNewRecordData(sensor, beacon, distance);
    }

    private void updateSensorRecord(Record lastKnowBeaconRecord, SensorRecord sensorRecord, Sensor sensor, Beacon beacon) {
        recordRepository.delete(lastKnowBeaconRecord);
        saveNewRecordData(sensor, beacon, sensorRecord.getDistance());
    }

    private void saveNewRecordData(Sensor sensor, Beacon beacon, double distance) {
        RecordId recordId = new RecordId(sensor, beacon, LocalDateTime.now());
        recordRepository.save(new Record(recordId, distance));
    }
}
