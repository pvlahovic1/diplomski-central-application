package hr.foi.diplomski.central.controllers.api.device;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceDto;
import hr.foi.diplomski.central.service.device.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;


    @GetMapping("/free")
    public ResponseEntity<List<DeviceDto>> getAllFreeDevices() {
        return ResponseEntity.ok(deviceService.findAllFreeDevices());
    }

}
