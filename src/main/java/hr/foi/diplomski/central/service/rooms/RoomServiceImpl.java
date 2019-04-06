package hr.foi.diplomski.central.service.rooms;

import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.mappers.RoomMapper;
import hr.foi.diplomski.central.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public List<RoomViewDto> getAllRoomsView() {
        return roomMapper.entitysToDtos(roomRepository.findAll());
    }
}
