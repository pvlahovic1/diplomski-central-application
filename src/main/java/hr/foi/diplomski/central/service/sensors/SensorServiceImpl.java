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
import hr.foi.diplomski.central.service.mqtt.MqttService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorToViewMapper sensorToViewMapper;
    private final SensorToDtoMapper sensorToDtoMapper;
    private final EntityResolver entityResolver;
    private final MqttService mqttService;

    @Override
    public Sensor updateSensor(SensorOutDto sensorOutDto) {
        if (sensorOutDto.getBeaconDataPurgeInterval() <= sensorOutDto.getBeaconDataSendInterval()) {
            throw new BadRequestException("Beacon data purge interval cannot be greater then beacon data send interval");
        }

        var sensorOptional = sensorRepository.findBySensorId(sensorOutDto.getDeviceId());

        if (sensorOptional.isPresent()) {
            Sensor sensor = sensorOptional.get();

            sensor.setSensorName(sensorOutDto.getDeviceName());
            sensor.setBeaconDataPurgeInterval(sensorOutDto.getBeaconDataPurgeInterval());
            sensor.setBeaconDataSendInterval(sensorOutDto.getBeaconDataSendInterval());

            return sensorRepository.save(sensor);
        } else {
            throw new BadRequestException(String
                    .format("Sensor with id: %s does not exist", sensorOutDto.getDeviceId()));
        }
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

        if (sensor.getId() == 0 || sensor.getSensorId() == null) {
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
                log.error("Error while sending mqtt message: {}", e);
                // TODO: throw internal server error
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
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


}
