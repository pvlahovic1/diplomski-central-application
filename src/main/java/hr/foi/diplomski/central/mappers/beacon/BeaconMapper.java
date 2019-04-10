package hr.foi.diplomski.central.mappers.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.mappers.DeviceMapper;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.repository.DeviceRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class, DeviceMapper.class})
public abstract class BeaconMapper {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    public abstract BeaconDto entityToDto(Beacon beacon);

    public abstract List<BeaconDto> entitysToDto(List<Beacon> beacons);

    @Mapping(target = "device", ignore = true)
    public abstract Beacon dtoToEntity(BeaconDto beaconDto);
}
