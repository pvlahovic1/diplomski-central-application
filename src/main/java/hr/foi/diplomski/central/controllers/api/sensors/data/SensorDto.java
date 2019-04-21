package hr.foi.diplomski.central.controllers.api.sensors.data;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SensorDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Integer beaconDataPurgeInterval;
    @NotNull
    private Integer beaconDataSendInterval;
    
}
