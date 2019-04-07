package hr.foi.diplomski.central.mappers;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;
import hr.foi.diplomski.central.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceMapper {

    @Mapping(source = "deviceName", target = "name")
    DeviceDto entityToDto(Device device);

    List<DeviceDto> entitysToDtos(Collection<Device> devices);
}
