package hr.foi.diplomski.central.controllers.api.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;
import hr.foi.diplomski.central.service.device.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deviceService.findDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<DeviceDto> saveNewDevice(@Valid @RequestBody DeviceDto dto) {
        return ResponseEntity.ok(deviceService.saveNewDevice(dto));
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable("id") Long id) {
        deviceService.deleteDevice(id);
    }
}
