package hr.foi.diplomski.central.controllers.api.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceSaveDto;
import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import hr.foi.diplomski.central.service.device.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceViewDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceSaveDto> getDeviceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deviceService.findById(id));
    }

    @GetMapping("/free")
    public ResponseEntity<List<DeviceViewDto>> getAllFreeDevices() {
        return ResponseEntity.ok(deviceService.findAllFreeDevices());
    }

    @PostMapping
    public ResponseEntity<DeviceSaveDto> saveDevice(@RequestBody DeviceSaveDto deviceSaveDto) {
        return ResponseEntity.ok(deviceService.saveDevice(deviceSaveDto));
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable("id") Long id) {
        deviceService.deleteDevice(id);
    }
}
