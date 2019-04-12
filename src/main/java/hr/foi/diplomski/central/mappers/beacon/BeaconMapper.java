package hr.foi.diplomski.central.mappers.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.mappers.device.DeviceMapper;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.repository.DeviceRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class, DeviceMapper.class})
public abstract class BeaconMapper {

    @Mapping(target = "device", ignore = true)
    public abstract BeaconDto entityToDto(Beacon beacon);

    public abstract List<BeaconDto> entitysToDto(List<Beacon> beacons);

    @AfterMapping
    public BeaconDto afterEntityToDto(Beacon beacon, @MappingTarget BeaconDto beaconDto) {
        if (beacon.getDevice() != null) {
            beaconDto.setDevice(beacon.getDevice().getDeviceName());
        } else {
            beaconDto.setDevice("");
        }

        return beaconDto;
    }


    @Mapping(target = "device", ignore = true)
    public abstract Beacon dtoToEntity(BeaconDto beaconDto);

    @Mapping(target = "itemName", ignore = true)
    public abstract BeaconViewDto entityToViewDto(Beacon beacon);

    public abstract List<BeaconViewDto> entitysToViewsDto(List<Beacon> beacon);

    @AfterMapping
    public BeaconViewDto afterEntityToViewDto(Beacon beacon, @MappingTarget BeaconViewDto beaconViewDto) {
        StringBuilder sb = new StringBuilder(beacon.getUuid());
        sb.append(" ").append(beacon.getMajor()).append(" ").append(beacon.getMinor());
        beaconViewDto.setItemName(sb.toString());

        return beaconViewDto;
    }
}
