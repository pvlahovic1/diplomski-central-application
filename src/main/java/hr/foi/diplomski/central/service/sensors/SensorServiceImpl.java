package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.out.SensorOutDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.mappers.sensor.SensorMapper;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.SensorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;

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
        return sensorMapper.entitysToDtos(sensorRepository.findByRoomId(roomId));
    }

    @Override
    public List<SensorViewDto> getAllFreeSensors() {
        return sensorMapper.entitysToDtos(sensorRepository.findAllByRoomIsNull());
    }

    @Override
    public List<SensorViewDto> getAllSensors() {
        return sensorMapper.entitysToDtos(sensorRepository.findAll());
    }

    @Override
    public void deleteSensor(Long senzorId) {
        sensorRepository.deleteById(senzorId);
    }


}
