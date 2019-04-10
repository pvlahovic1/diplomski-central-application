package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.sensor.SensorToDtoMapper;
import hr.foi.diplomski.central.mappers.sensor.SensorToViewMapper;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorToViewMapper sensorToViewMapper;
    private final SensorToDtoMapper sensorToDtoMapper;

    @Override
    public Sensor updateSensor(SensorOutDto sensorOutDto) throws BadDataException {
        if (sensorOutDto.getBeaconDataPurgeInterval() <= sensorOutDto.getBeaconDataSendInterval()) {
            throw new BadDataException("Beacon data purge interval cannot be greater then beacon data send interval");
        }

        var sensorOptional = sensorRepository.findBySensorId(sensorOutDto.getDeviceId());

        if (sensorOptional.isPresent()) {
            Sensor sensor = sensorOptional.get();

            sensor.setSensorName(sensorOutDto.getDeviceName());
            sensor.setBeaconDataPurgeInterval(sensorOutDto.getBeaconDataPurgeInterval());
            sensor.setBeaconDataSendInterval(sensorOutDto.getBeaconDataSendInterval());

            return sensorRepository.save(sensor);
        } else {
            throw new BadDataException("Sensor with id:" + sensorOutDto.getDeviceId() + " does not exist");
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
        Sensor sensor = sensorToDtoMapper.dtoToEntity(sensorDto);
        sensor.setSensorId(calculatesensorId(sensor));
        sensor.setBeaconDataSendInterval(sensor.getBeaconDataPurgeInterval() * 1000);
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

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            log.warn("There is no MessageDigest SHA-256");
        }

        return null;
    }


}
