package hr.foi.diplomski.central.service.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.device.DeviceMapper;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Device;
import hr.foi.diplomski.central.model.Room;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final BeaconRepository beaconRepository;
    private final RecordRepository recordRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public List<DeviceDto> findAllDevicesInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).get();
        List<Sensor> sensors = room.getSensors();

        if (sensors.isEmpty()) {
            return Collections.emptyList();
        }

        List<Beacon> beacons = beaconRepository.findAll();
        List<Record> validRecords = new ArrayList<>();

        LocalDateTime validTime = LocalDateTime.now().minusMinutes(1L);

        beacons.forEach(e -> recordRepository.findFirstByRecordId_BeaconAndRecordId_RecordDateBetweenOrderByDistanceAsc(
                e, validTime, LocalDateTime.now()).ifPresent(validRecords::add));

        List<Record> recordsBySensor = validRecords.stream()
                .filter(e -> sensors.contains(e.getRecordId().getSensor())).collect(Collectors.toList());

        List<Device> devices = new ArrayList<>();
        recordsBySensor.forEach(e -> devices.add(e.getRecordId().getBeacon().getDevice()));

        return deviceMapper.entitysToDtos(devices);
    }

    @Override
    public List<DeviceDto> findAllDevices() {
        return deviceMapper.entitysToDtos(deviceRepository.findAll());
    }

    @Override
    public DeviceDto findDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Uređaj s id: %s ne postoji", id)));

        return deviceMapper.entityToDto(device);
    }

    @Override
    public DeviceDto saveNewDevice(DeviceDto dto) {
        Device device = deviceMapper.dtoToEntity(dto);

        return deviceMapper.entityToDto(deviceRepository.save(device));
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Uređaj s id: %s ne postoji", id)));

        deviceRepository.delete(device);
    }
}
