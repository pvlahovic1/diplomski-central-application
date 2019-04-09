package hr.foi.diplomski.central.mappers.sensor;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SensorToDtoMapper {

    @Mapping(source = "sensorName", target = "name")
    SensorDto entityToDto(Sensor entity);

    @Mapping(source = "name", target = "sensorName")
    Sensor dtoToEntity(SensorDto sensorDto);

    List<SensorDto> entitysToDtos(List<Sensor> entitys);

}
