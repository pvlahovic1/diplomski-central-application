package hr.foi.diplomski.central.controllers.api.sensors.data;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class SensorDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Max(60)
    private Integer beaconDataPurgeInterval;
    @NotNull
    @Max(59)
    private Integer beaconDataSendInterval;
    private boolean present;
    
}
