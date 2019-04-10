package hr.foi.diplomski.central.mappers.reslovers;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Device;
import hr.foi.diplomski.central.model.Room;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import hr.foi.diplomski.central.repository.RoomRepository;
import hr.foi.diplomski.central.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityResolver {

    private final RoomRepository roomRepository;
    private final SensorRepository sensorRepository;
    private final DeviceRepository deviceRepository;
    private final BeaconRepository beaconRepository;

    @ObjectFactory
    public Room resolve(RoomDto dto, @TargetType Class<Room> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? roomRepository.findById(dto.getId()).get() : new Room();
    }

    @ObjectFactory
    public Sensor resolve(SensorViewDto dto, @TargetType Class<Sensor> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? sensorRepository.findById(dto.getId()).get() : new Sensor();
    }

    @ObjectFactory
    public Device resolve(DeviceDto dto, @TargetType Class<Device> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? deviceRepository.findById(dto.getId()).get() : new Device();
    }

    @ObjectFactory
    public Beacon resolve(BeaconDto dto, @TargetType Class<Beacon> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? beaconRepository.findById(dto.getId()).get() : new Beacon();
    }

}
