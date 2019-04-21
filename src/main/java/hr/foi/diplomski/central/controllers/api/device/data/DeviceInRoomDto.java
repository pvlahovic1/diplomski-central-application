package hr.foi.diplomski.central.controllers.api.device.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceInRoomDto {
    private DeviceViewDto deviceInfo;
    private LocalDateTime dateTime;
    private Double distance;
}
