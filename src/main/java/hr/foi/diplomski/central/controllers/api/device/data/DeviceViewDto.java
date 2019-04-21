package hr.foi.diplomski.central.controllers.api.device.data;

import lombok.Data;

@Data
public class DeviceViewDto {
    private Long id;
    private String name;
    private String beaconData;
}
