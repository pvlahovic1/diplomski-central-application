package hr.foi.diplomski.central.mappers.sensor;


import hr.foi.diplomski.central.controllers.api.sensors.data.SensorViewDto;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class})
public abstract class SensorToViewMapper {

    @Mapping(source = "sensorName", target = "name")
    @Mapping(source = "room.roomName", target = "roomName")
    @Mapping(target = "present", ignore = true)
    public abstract SensorViewDto entityToDto(Sensor entity);

    public abstract List<SensorViewDto> entitysToDtos(List<Sensor> entitys);

    @Mapping(target = "lastTimePresent", ignore = true)
    public abstract Sensor dtoToEntity(SensorViewDto sensorViewDto);

    public abstract List<Sensor> dtosToEntity(List<SensorViewDto> dtos);

    @AfterMapping
    SensorViewDto afterEntityToDto(Sensor sensor, @MappingTarget SensorViewDto sensorViewDto) {
        if (StringUtils.isBlank(sensorViewDto.getRoomName())) {
            sensorViewDto.setRoomName("Nije postavljena");
        }

        if (sensorViewDto.getBeaconDataPurgeInterval() != null) {
            sensorViewDto.setBeaconDataPurgeInterval(sensorViewDto.getBeaconDataPurgeInterval() / 1000);
        }
        if (sensorViewDto.getBeaconDataSendInterval() != null) {
            sensorViewDto.setBeaconDataSendInterval(sensorViewDto.getBeaconDataSendInterval() / 1000);
        }

        sensorViewDto.setPresent(CommonUtils.isSensorActive(Optional.ofNullable(sensor.getLastTimePresent())));

        return sensorViewDto;
    }

    @AfterMapping
    Sensor afterDtoToEntity(SensorViewDto sensorViewDto, @MappingTarget Sensor sensor) {
        if (sensorViewDto.getBeaconDataSendInterval() != null) {
            sensor.setBeaconDataSendInterval(sensorViewDto.getBeaconDataSendInterval() * 1000);
        }

        if (sensorViewDto.getBeaconDataPurgeInterval() != null) {
            sensor.setBeaconDataPurgeInterval(sensorViewDto.getBeaconDataPurgeInterval() * 1000);
        }

        return sensor;
    }
}
