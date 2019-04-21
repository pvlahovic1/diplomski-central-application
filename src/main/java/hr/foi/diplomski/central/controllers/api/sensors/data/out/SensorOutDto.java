package hr.foi.diplomski.central.controllers.api.sensors.data.out;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SensorOutDto {

    @NotNull
    @Length(min = 64, max = 65)
    private String deviceId;
    @NotNull
    private String deviceName;
    @NotNull
    @Min(100)
    private Integer beaconDataPurgeInterval;
    @NotNull
    @Min(100)
    private Integer beaconDataSendInterval;

}
