package hr.foi.diplomski.central.mappers;


import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SensorMapper {

    @Mapping(source = "sensorName", target = "name")
    SensorViewDto entityToDto(Sensor entity);

    List<SensorViewDto> entitysToDtos(List<Sensor> entitys);

}
