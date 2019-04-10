package hr.foi.diplomski.central.service.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;

import java.util.List;

public interface BeaconService {

    List<BeaconDto> findAllBeacons();

    BeaconDto findBeaconById(Long id);

    BeaconDto saveNewBeacon(BeaconDto beaconDto);

    void deleteBeaon(Long idBeacon);

}