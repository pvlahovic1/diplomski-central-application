package hr.foi.diplomski.central.mappers;

import hr.foi.diplomski.central.controllers.api.room.data.RoomDto;
import hr.foi.diplomski.central.controllers.api.room.data.RoomViewDto;
import hr.foi.diplomski.central.mappers.reslovers.RoomResolver;
import hr.foi.diplomski.central.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {RoomResolver.class, SensorMapper.class})
public interface RoomMapper {

    @Mapping(source = "roomName", target = "itemName")
    RoomViewDto entityToViewDto(Room room);

    RoomDto entityToDto(Room dto);

    List<RoomViewDto> entitysToViewDtos(List<Room> rooms);

    @Mapping(source = "name", target = "roomName")
    Room dtoToEntity(RoomDto dto);
}
