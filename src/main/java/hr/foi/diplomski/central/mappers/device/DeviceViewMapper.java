package hr.foi.diplomski.central.mappers.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.mappers.beacon.BeaconMapper;
import hr.foi.diplomski.central.mappers.reslovers.EntityResolver;
import hr.foi.diplomski.central.mappers.user.UserMapper;
import hr.foi.diplomski.central.model.Device;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EntityResolver.class, UserMapper.class, BeaconMapper.class})
public abstract class DeviceViewMapper {

    @Autowired
    private BeaconMapper beaconMapper;

    @Mapping(source = "deviceName", target = "name")
    @Mapping(source = "beacon.uuid", target = "beaconData")
    public abstract DeviceViewDto entityToDtoView(Device device);

    public abstract List<DeviceViewDto> entitysToViews(List<Device> devices);

    @Mapping(source = "name", target = "deviceName")
    public abstract Device dtoToEntity(DeviceViewDto deviceViewDto);

    @AfterMapping
    public DeviceViewDto aferEntityToDtoView(Device device, @MappingTarget DeviceViewDto deviceViewDto) {
        if (device.getBeacon() != null) {
            deviceViewDto.setBeaconData(device.getBeacon().getUuid() + " " + device.getBeacon().getMajor()
                    + " " + device.getBeacon().getMinor());
        } else {
            deviceViewDto.setBeaconData("Beacon nije postavljen");
        }

        return deviceViewDto;
    }

    public abstract DeviceSaveDto entityToSaveDto(Device device);

    public abstract Device saveDtoToEntity(DeviceSaveDto deviceSaveDto);

    @AfterMapping
    public DeviceSaveDto afterEntityToSaveDto(Device device, @MappingTarget DeviceSaveDto deviceSaveDto) {
        if (device.getBeacon() != null) {
            deviceSaveDto.setBeaconView(beaconMapper.entityToViewDto(device.getBeacon()));
        }

        return deviceSaveDto;
    }

}
