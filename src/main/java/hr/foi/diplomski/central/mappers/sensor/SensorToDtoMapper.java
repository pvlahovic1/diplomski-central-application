package hr.foi.diplomski.central.mappers.sensor;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.model.Sensor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SensorToDtoMapper {

    @Mapping(source = "sensorName", target = "name")
    public abstract SensorDto entityToDto(Sensor entity);

    @Mapping(source = "name", target = "sensorName")
    public abstract Sensor dtoToEntity(SensorDto sensorDto);

    public abstract List<SensorDto> entitysToDtos(List<Sensor> entitys);

    @AfterMapping
    public SensorDto afterToDto(Sensor sensor, @MappingTarget SensorDto sensorDto) {
        if (sensorDto.getBeaconDataPurgeInterval() != null) {
            sensorDto.setBeaconDataPurgeInterval(sensorDto.getBeaconDataPurgeInterval() / 1000);
        }
        if (sensorDto.getBeaconDataSendInterval() != null) {
            sensorDto.setBeaconDataSendInterval(sensorDto.getBeaconDataSendInterval() / 1000);
        }

        return sensorDto;
    }

}
