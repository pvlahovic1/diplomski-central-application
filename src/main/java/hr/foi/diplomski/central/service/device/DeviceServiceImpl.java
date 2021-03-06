package hr.foi.diplomski.central.service.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoom;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoomDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.beacon.BeaconMapper;
import hr.foi.diplomski.central.mappers.device.DeviceInRoomMapper;
import hr.foi.diplomski.central.mappers.device.DeviceViewMapper;
import hr.foi.diplomski.central.model.*;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.repository.RoomRepository;
import hr.foi.diplomski.central.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    @Value("${beacon.record.duration}")
    private Long maxRecordDuration;

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final BeaconRepository beaconRepository;
    private final RecordRepository recordRepository;
    private final DeviceViewMapper deviceMapper;
    private final BeaconMapper beaconMapper;
    private final DeviceInRoomMapper devicesInRoomMapper;
    private final UserService userService;

    @Override
    public List<DeviceViewDto> findAllFreeDevices() {
        return deviceMapper.entitysToViews(deviceRepository.findAllByUserAndBeaconIsNull(userService.findCurrentUser()));
    }

    @Override
    public List<DeviceViewDto> findAllDevices() {
        return deviceMapper.entitysToViews(deviceRepository.findAllByUser(userService.findCurrentUser()));
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Uredaj s id %s ne postoji", id)));

        User user = userService.findCurrentUser();

        if (user.equals(device.getUser())) {

            if (device.getBeacon() != null) {
                device.setBeacon(null);
            }

            deviceRepository.delete(device);
        } else {
            throw new BadRequestException(String
                    .format("Uredaj s id %s nije u vlasništvu korisnika %s", id, user.getUsername()));
        }
    }

    @Override
    public List<DeviceInRoomDto> findAllDevicesInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException(String.format("Ne postoji prostorija s id %s", roomId)));
        List<Sensor> sensors = room.getSensors();

        if (sensors.isEmpty()) {
            return Collections.emptyList();
        }

        List<Beacon> beacons = beaconRepository.findAll();
        List<Record> validRecords = new ArrayList<>();

        LocalDateTime validTime = LocalDateTime.now().minusMinutes(maxRecordDuration);

        beacons.forEach(e -> recordRepository.findFirstByRecordId_BeaconAndRecordId_RecordDateBetweenOrderByDistanceAsc(
                e, validTime, LocalDateTime.now()).ifPresent(validRecords::add));

        List<Record> recordsBySensor = validRecords.stream()
                .filter(e -> sensors.contains(e.getRecordId().getSensor())).collect(Collectors.toList());

        List<DeviceInRoom> devicesInRoom = new ArrayList<>();
        recordsBySensor.forEach(e -> e.getRecordId().getBeacon().getDevices().forEach(device -> {
            devicesInRoom.add(new DeviceInRoom(device, e.getRecordId().getRecordDate(), e.getDistance()));
        }));

        return devicesInRoomMapper.objectsToDtos(devicesInRoom);
    }

    @Override
    public DeviceSaveDto saveDevice(DeviceSaveDto deviceSaveDto) {
        Device device = deviceMapper.saveDtoToEntity(deviceSaveDto);
        User user = userService.findCurrentUser();

        if (device.getId() != null && device.getId() != 0) {
            if (user.equals(device.getUser())) {
                return doSaveDevice(device, deviceSaveDto);
            } else {
                throw new BadRequestException(String
                        .format("Uredaj s id %s nije u vlasništvu korisnika %s", device.getId(), user.getUsername()));
            }
        } else {
            device.setUser(user);
            return doSaveDevice(device, deviceSaveDto);
        }
    }

    @Override
    public DeviceSaveDto findById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Uredaj s id %s ne postoji", id)));

        User user = userService.findCurrentUser();

        if (user.equals(device.getUser())) {
            return deviceMapper.entityToSaveDto(device);
        } else {
            throw new BadRequestException(String.format("Korisnik %s nije vlasnik uređaja %s", user.getUsername(),
                    device.getId()));
        }
    }

    private DeviceSaveDto doSaveDevice(Device device, DeviceSaveDto deviceSaveDto) {
        if (deviceSaveDto.getBeaconView() != null) {
            Beacon beacon = beaconMapper.viewDtoToEntity(deviceSaveDto.getBeaconView());
            device.setBeacon(beacon);
        } else {
            device.setBeacon(null);
        }

        device = deviceRepository.save(device);

        return deviceMapper.entityToSaveDto(device);
    }
}
