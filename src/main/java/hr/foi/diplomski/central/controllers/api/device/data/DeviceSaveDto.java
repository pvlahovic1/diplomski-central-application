package hr.foi.diplomski.central.controllers.api.device.data;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import lombok.Data;

@Data
public class DeviceSaveDto {
    private Long id;
    private String deviceName;
    private BeaconViewDto beaconView;
}
