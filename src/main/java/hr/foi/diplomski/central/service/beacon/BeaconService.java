package hr.foi.diplomski.central.service.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;

import java.util.List;

public interface BeaconService {

    List<BeaconDto> findAllBeacons();

    BeaconDto findBeaconById(Long id);

    BeaconDto saveBeacon(BeaconDto beaconDto);

    void deleteBeaon(Long idBeacon);

    List<BeaconViewDto> findAllFreeBeacons();
}
