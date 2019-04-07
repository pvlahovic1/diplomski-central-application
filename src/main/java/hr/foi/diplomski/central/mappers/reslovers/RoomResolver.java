package hr.foi.diplomski.central.mappers.reslovers;

import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.model.Room;
import hr.foi.diplomski.central.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomResolver {

    private final RoomRepository roomRepository;

    @ObjectFactory
    public Room resolve(RoomDto dto, @TargetType Class<Room> type) {
        return dto != null && dto.getId() != null && dto.getId() != 0L ? roomRepository.findById(dto.getId()).get() : new Room();
    }
}
