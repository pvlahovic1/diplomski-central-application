package hr.foi.diplomski.central.service.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceInRoomDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;

import java.util.List;

public interface DeviceService {

    List<DeviceViewDto> findAllFreeDevices();

    List<DeviceViewDto> findAllDevices();

    void deleteDevice(Long id);

    List<DeviceInRoomDto> findAllDevicesInRoom(Long roomId);

    DeviceSaveDto saveDevice(DeviceSaveDto deviceSaveDto);

    DeviceSaveDto findById(Long id);

}
