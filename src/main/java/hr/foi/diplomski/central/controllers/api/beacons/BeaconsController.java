package hr.foi.diplomski.central.controllers.api.beacons;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.service.beacon.BeaconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beacons")
public class BeaconsController {

    private final BeaconService beaconService;

    @GetMapping
    public ResponseEntity<List<BeaconDto>> getAllBeacons() {
        return ResponseEntity.ok(beaconService.findAllBeacons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeaconDto> getBeaconById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(beaconService.findBeaconById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteBeacon(@PathVariable("id") Long id) {
        beaconService.deleteBeaon(id);
    }

    @PostMapping
    public ResponseEntity<BeaconDto> saveNewBeacon(@RequestBody BeaconDto beaconDto) {
        return ResponseEntity.ok(beaconService.saveNewBeacon(beaconDto));
    }

    @GetMapping("/free")
    public ResponseEntity<List<BeaconViewDto>> findAllFreeBeacons() {
        return ResponseEntity.ok(beaconService.findAllFreeBeacons());
    }

}
