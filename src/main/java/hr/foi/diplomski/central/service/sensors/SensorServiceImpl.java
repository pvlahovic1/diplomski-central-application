package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.mappers.sensor.SensorToDtoMapper;
import hr.foi.diplomski.central.mappers.sensor.SensorToViewMapper;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.SensorRepository;
import hr.foi.diplomski.central.service.mqtt.service.MqttService;
import hr.foi.diplomski.central.service.socket.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
@AllArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorToViewMapper sensorToViewMapper;
    private final SensorToDtoMapper sensorToDtoMapper;
    private final EntityResolver entityResolver;
    private final MqttService mqttService;
    private final WebSocketService webSocketService;

    @Override
    public Sensor updateSensor(SensorOutDto sensorOutDto) {
        if (sensorOutDto.getBeaconDataPurgeInterval() <= sensorOutDto.getBeaconDataSendInterval()) {
            throw new BadRequestException("Beacon data purge interval cannot be greater then beacon data send interval");
        }

        Sensor sensor = sensorRepository.findBySensorId(sensorOutDto.getDeviceId()).orElseThrow(() -> new BadRequestException(String
                .format("Sensor with id: %s does not exist", sensorOutDto.getDeviceId())));

        sensor.setSensorName(sensorOutDto.getDeviceName());
        sensor.setBeaconDataPurgeInterval(sensorOutDto.getBeaconDataPurgeInterval());
        sensor.setBeaconDataSendInterval(sensorOutDto.getBeaconDataSendInterval());

        sensor = sensorRepository.save(sensor);

        webSocketService.refreshSensorState(sensor.getId());

        return sensor;
    }

    @Override
    public List<SensorViewDto> getAllSensorsViewByRoom(Long roomId) {
        return sensorToViewMapper.entitysToDtos(sensorRepository.findByRoomId(roomId));
    }

    @Override
    public List<SensorViewDto> getAllFreeSensors() {
        return sensorToViewMapper.entitysToDtos(sensorRepository.findAllByRoomIsNull());
    }

    @Override
    public List<SensorViewDto> getAllSensors() {
        return sensorToViewMapper.entitysToDtos(sensorRepository.findAll());
    }

    @Override
    public SensorDto getSensordById(Long id) {
        Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new BadRequestException(String
                .format("Ne postoji senzor s id: %s", id)));

        return sensorToDtoMapper.entityToDto(sensor);
    }

    @Override
    public SensorDto saveSensor(SensorDto sensorDto) {
        Sensor sensor = entityResolver.resolve(sensorDto, Sensor.class);

        if (sensorDto.getBeaconDataPurgeInterval() <= sensorDto.getBeaconDataSendInterval()) {
            throw new BadRequestException("Itreval brisanja podataka mora biti veći od intervala slanja podataka.");
        }

        if (sensor.getSensorId() == null || sensor.getId() == 0) {
            return saveNewSensor(sensorDto);
        } else {
            try {
                if (!StringUtils.equals(sensor.getSensorName(), sensorDto.getName())) {
                    sensor.setSensorName(sensorDto.getName());
                    mqttService.updateSensorName(sensor.getSensorId(), sensor.getSensorName());
                }

                if (!sensor.getBeaconDataSendInterval().equals(sensorDto.getBeaconDataSendInterval() * 1000)
                        || !sensor.getBeaconDataPurgeInterval().equals(sensorDto.getBeaconDataPurgeInterval() * 1000)) {
                    sensor.setBeaconDataSendInterval(sensorDto.getBeaconDataSendInterval() * 1000);
                    sensor.setBeaconDataPurgeInterval(sensorDto.getBeaconDataPurgeInterval() * 1000);

                    mqttService.updateSensorIntervals(sensor.getSensorId(), sensor.getBeaconDataPurgeInterval(),
                            sensor.getBeaconDataSendInterval());
                }
            } catch (Exception e) {
                log.error("Error while sending mqtt message:", e);
            }

            return sensorToDtoMapper.entityToDto(sensor);
        }
    }

    private SensorDto saveNewSensor(SensorDto sensorDto) {
        Sensor sensor = sensorToDtoMapper.dtoToEntity(sensorDto);
        sensor.setSensorId(calculatesensorId(sensor));
        sensor.setBeaconDataSendInterval(sensor.getBeaconDataSendInterval() * 1000);
        sensor.setBeaconDataPurgeInterval(sensor.getBeaconDataPurgeInterval() * 1000);
        return sensorToDtoMapper.entityToDto(sensorRepository.save(sensor));
    }

    @Override
    public void deleteSensor(Long senzorId) {
        sensorRepository.deleteById(senzorId);
    }

    @Override
    public HttpEntity<byte[]> createCongifurationFile(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new BadRequestException(String
                .format("Ne posotji senzor s id %s", sensorId)));

        StringJoiner stringJoiner = new StringJoiner("\n");

        stringJoiner.add(String.format("deviceId=%s", sensor.getSensorId()));
        stringJoiner.add(String.format("deviceName=%s", sensor.getSensorName()));
        stringJoiner.add(String.format("username=%s", "sensor_device"));
        stringJoiner.add(String.format("password=%s", "sensor_device_password"));
        stringJoiner.add(String.format("beaconDataPurgeInterval=%s", sensor.getBeaconDataPurgeInterval()));
        stringJoiner.add(String.format("beaconDataSendInterval=%s", sensor.getBeaconDataSendInterval()));
        stringJoiner.add(String.format("mqttTopicUrl=%s", ""));
        stringJoiner.add(String.format("mqttTopicTitle=%s", "listener_settings"));
        stringJoiner.add(String.format("centralApplicationUrl=%s", "http://localhost:8080"));
        stringJoiner.add(String.format("centralApplicationBeaconPath=%s", "/api/records"));
        stringJoiner.add(String.format("centralApplicationDevicePath=%s", "/api/sensors"));
        stringJoiner.add(String.format("centralApplicationAuthenticationPath=%s", "/api/authenticate"));

        byte[] document = stringJoiner.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.set("Content-Disposition", "inline; filename=" + "listener_configuration.conf");
        header.setContentLength(document.length);

        return new HttpEntity<>(document, header);
    }

    @Override
    public void updateSensorLasPresentTime(Sensor sensor) {
        sensor.setLastTimePresent(LocalDateTime.now());
        sensorRepository.save(sensor);
    }

    private String calculatesensorId(Sensor sensor) {
        StringBuilder sb = new StringBuilder(sensor.getSensorName());
        sb.append(LocalDateTime.now().toString());

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            log.warn("There is no MessageDigest SHA-256");
        }

        return null;
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte hash1 : hash) {
            String hex = Integer.toHexString(0xff & hash1);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
