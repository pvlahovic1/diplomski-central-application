package hr.foi.diplomski.central.mappers;

import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {

    @Mapping(source = "roomName", target = "itemName")
    RoomViewDto entityToDto(Room room);

    List<RoomViewDto> entitysToDtos(List<Room> rooms);
}
