package hr.foi.diplomski.central.controllers.api.beacons.dto;

import hr.foi.diplomski.central.controllers.api.device.data.DeviceViewDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BeaconDto {

    private Long id;

    @NotNull
    @Length(min = 36, max = 36)
    private String uuid;

    @NotNull
    @Min(1)
    private Integer major;

    @NotNull
    @Min(1)
    private Integer minor;

    private List<DeviceViewDto> devices;
}
