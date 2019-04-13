package hr.foi.diplomski.central.controllers.api.device.data;

import hr.foi.diplomski.central.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInRoom{
    private Device deviceInfo;
    private LocalDateTime timestamp;
    private Double distance;
}
