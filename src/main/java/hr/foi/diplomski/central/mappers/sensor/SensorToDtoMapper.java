package hr.foi.diplomski.central.mappers.sensor;

import hr.foi.diplomski.central.controllers.api.sensors.data.SensorDto;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.utils.CommonUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
, uses = {EntityResolver.class})
public abstract class SensorToDtoMapper {

    @Mapping(source = "sensorName", target = "name")
    @Mapping(target = "present", ignore = true)
    public abstract SensorDto entityToDto(Sensor entity);

    @Mapping(source = "name", target = "sensorName")
    @Mapping(target = "lastTimePresent", ignore = true)
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

        sensorDto.setPresent(CommonUtils.isSensorActive(Optional.ofNullable(sensor.getLastTimePresent())));

        return sensorDto;
    }

}
