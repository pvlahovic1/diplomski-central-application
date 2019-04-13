package hr.foi.diplomski.central.mappers.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.mappers.device.DeviceViewMapper;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Beacon;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class, DeviceViewMapper.class})
public abstract class BeaconMapper {

    public abstract BeaconDto entityToDto(Beacon beacon);

    public abstract List<BeaconDto> entitysToDto(List<Beacon> beacons);

    public abstract Beacon dtoToEntity(BeaconDto beaconDto);

    @Mapping(target = "itemName", ignore = true)
    public abstract BeaconViewDto entityToViewDto(Beacon beacon);

    public abstract List<BeaconViewDto> entitysToViewDtos(List<Beacon> beacon);

    public abstract Beacon viewDtoToEntity(BeaconViewDto beaconViewDto);

    public abstract List<Beacon> viewDtoToEntity(List<BeaconViewDto> beaconViewDto);

    @AfterMapping
    public BeaconViewDto afterEntityToViewDto(Beacon beacon, @MappingTarget BeaconViewDto beaconViewDto) {
        beaconViewDto.setItemName(beacon.getUuid() + " " + beacon.getMajor() + " " + beacon.getMinor());

        return beaconViewDto;
    }
}
