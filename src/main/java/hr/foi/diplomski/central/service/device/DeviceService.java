package hr.foi.diplomski.central.service.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;

import java.util.List;

public interface DeviceService {

    List<DeviceDto> findAllDevicesInRoom(Long roomId);

}
