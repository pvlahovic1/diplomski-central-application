package hr.foi.diplomski.central.mappers.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoom;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoomDto;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class, DeviceViewMapper.class})
public interface DeviceInRoomMapper {

    DeviceInRoomDto objectToDto(DeviceInRoom deviceInRoom);

    List<DeviceInRoomDto> objectsToDtos(List<DeviceInRoom> deviceInRoom);

}
