package hr.foi.diplomski.central.mappers.reslovers;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.controllers.api.users.data.UserDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.model.*;
import hr.foi.diplomski.central.repository.*;
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
    private final UserRepository userRepository;

    @ObjectFactory
    public User resolve(UserDto dto, @TargetType Class<User> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? userRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Korisnik s id %s ne postoji", dto.getId()))) : new User();
    }


    @ObjectFactory
    public Room resolve(RoomDto dto, @TargetType Class<Room> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? roomRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Prostorija s id %s ne postoji", dto.getId()))) : new Room();
    }

    @ObjectFactory
    public Sensor resolve(SensorViewDto dto, @TargetType Class<Sensor> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? sensorRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Senzor s id %s ne postoji", dto.getId()))): new Sensor();
    }

    @ObjectFactory
    public Sensor resolve(SensorDto dto, @TargetType Class<Sensor> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? sensorRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Senzor s id %s ne postoji", dto.getId()))): new Sensor();
    }

    @ObjectFactory
    public Beacon resolve(BeaconDto dto, @TargetType Class<Beacon> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? beaconRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Beacon s id %s ne postoji", dto.getId()))): new Beacon();
    }

    @ObjectFactory
    public Beacon resolve(BeaconViewDto dto, @TargetType Class<Beacon> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? beaconRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Beacon s id %s ne postoji", dto.getId()))): new Beacon();
    }

    @ObjectFactory
    public Device resolve(DeviceViewDto dto, @TargetType Class<Device> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? deviceRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Uredaj s id %s ne postoji", dto.getId()))) : new Device();
    }

    @ObjectFactory
    public Device resolve(DeviceSaveDto dto, @TargetType Class<Device> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? deviceRepository.findById(dto.getId())
                .orElseThrow(() -> new BadRequestException(String
                        .format("Uredaj s id %s ne postoji", dto.getId()))): new Device();
    }

}
