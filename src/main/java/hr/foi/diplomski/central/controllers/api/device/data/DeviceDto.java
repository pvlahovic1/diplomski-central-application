package hr.foi.diplomski.central.controllers.api.device.data;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.model.dto.UserDto;
import lombok.Data;

@Data
public class DeviceDto {

    private Long id;
    private String name;
    private BeaconDto beacon;
    private UserDto user;

}
