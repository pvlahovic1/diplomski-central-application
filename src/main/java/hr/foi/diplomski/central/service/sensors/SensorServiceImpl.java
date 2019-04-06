package hr.foi.diplomski.central.service.sensors;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.exceptions.BadDataException;
import hr.foi.diplomski.central.mappers.SensorMapper;
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
    public Sensor updateSensor(SensorDto sensorDto) throws BadDataException {
        if (sensorDto.getBeaconDataPurgeInterval() <= sensorDto.getBeaconDataSendInterval()) {
            throw new BadDataException("Beacon data purge interval cannot be greater then beacon data send interval");
        }

        var sensorOptional = sensorRepository.findBySensorId(sensorDto.getDeviceId());

        if (sensorOptional.isPresent()) {
            Sensor sensor = sensorOptional.get();

            sensor.setSensorName(sensorDto.getDeviceName());
            sensor.setBeaconDataPurgeInterval(sensorDto.getBeaconDataPurgeInterval());
            sensor.setBeaconDataSendInterval(sensorDto.getBeaconDataSendInterval());

            return sensorRepository.save(sensor);
        } else {
            throw new BadDataException("Sensor with id:" + sensorDto.getDeviceId() + " does not exist");
        }
    }

    @Override
    public List<SensorViewDto> getAllSensorsViewByRoom(Long roomId) {
        return sensorMapper.entitysToDtos(sensorRepository.findByRoomId(roomId));
    }


}
