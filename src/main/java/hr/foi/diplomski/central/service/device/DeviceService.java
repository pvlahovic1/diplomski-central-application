package hr.foi.diplomski.central.service.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;

import java.util.List;

public interface DeviceService {

    List<DeviceViewDto> findAllFreeDevices();

    List<DeviceViewDto> findAllDevices();

    void deleteDevice(Long id);

    List<DeviceViewDto> findAllDevicesInRoom(Long roomId);

    DeviceSaveDto saveDevice(DeviceSaveDto deviceSaveDto);

    DeviceSaveDto findById(Long id);

}
