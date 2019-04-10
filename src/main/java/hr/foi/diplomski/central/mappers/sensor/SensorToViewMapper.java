package hr.foi.diplomski.central.mappers.sensor;


import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Sensor;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class})
public abstract class SensorToViewMapper {

    @Mapping(source = "sensorName", target = "name")
    @Mapping(source = "room.roomName", target = "roomName")
    public abstract SensorViewDto entityToDto(Sensor entity);

    public abstract List<SensorViewDto> entitysToDtos(List<Sensor> entitys);

    public abstract Sensor dtoToEntity(SensorViewDto sensorViewDto);

    public abstract List<Sensor> dtosToEntity(List<SensorViewDto> dtos);

    @AfterMapping
    SensorViewDto afterEntityToDto(Sensor sensor, @MappingTarget SensorViewDto sensorViewDto) {
        if (StringUtils.isBlank(sensorViewDto.getRoomName())) {
            sensorViewDto.setRoomName("Nije postavljena");
        }

        return sensorViewDto;
    }
}
