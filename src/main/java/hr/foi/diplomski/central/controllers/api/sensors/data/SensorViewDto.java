package hr.foi.diplomski.central.controllers.api.sensors.data;

import lombok.Data;

@Data
public class SensorViewDto {

    private Long id;
    private String name;
    private Integer beaconDataPurgeInterval;
    private Integer beaconDataSendInterval;
    private String roomName;

}
