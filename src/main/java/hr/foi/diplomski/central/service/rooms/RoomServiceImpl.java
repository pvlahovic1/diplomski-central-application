package hr.foi.diplomski.central.service.rooms;

import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.mappers.RoomMapper;
import hr.foi.diplomski.central.model.Room;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RecordRepository recordRepository;

    @Override
    public RoomDto saveRoom(RoomDto roomDto) {
        Room room = roomMapper.dtoToEntity(roomDto);

        room = roomRepository.save(room);

        return roomMapper.entityToDto(room);
    }

    @Override
    public List<RoomViewDto> getAllRoomsView() {
        return roomMapper.entitysToViewDtos(roomRepository.findAll());
    }

    @Override
    public RoomDto getById(Long id) {
        Room room = roomRepository.findById(id).get();

        return roomMapper.entityToDto(room);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id).get();

        List<Sensor> sensors = room.getSensors();

        if (!sensors.isEmpty()) {
            recordRepository.deleteAllByRecordId_Sensor(sensors);
        }

        sensors.forEach(e -> e.setRoom(null));
        room.getSensors().clear();

        roomRepository.delete(room);
    }
}
