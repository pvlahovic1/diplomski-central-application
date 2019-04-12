package hr.foi.diplomski.central.service.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.beacon.BeaconMapper;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Device;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeaconServiceImpl implements BeaconService {

    private final BeaconRepository beaconRepository;
    private final DeviceRepository deviceRepository;
    private final BeaconMapper beaconMapper;

    @Override
    public List<BeaconDto> findAllBeacons() {
        return beaconMapper.entitysToDto(beaconRepository.findAll());
    }

    @Override
    public BeaconDto findBeaconById(Long id) {
        Beacon beacon = beaconRepository.findById(id).orElseThrow(() -> new BadRequestException(
                String.format("Ne postoji beacon s id: %s", id)));

        return beaconMapper.entityToDto(beacon);
    }

    @Override
    public BeaconDto saveNewBeacon(BeaconDto beaconDto) {
        Beacon beacon = beaconMapper.dtoToEntity(beaconDto);

        return beaconMapper.entityToDto(beaconRepository.save(beacon));
    }

    @Override
    public void deleteBeaon(Long idBeacon) {
        Beacon beacon = beaconRepository.findById(idBeacon)
                .orElseThrow(() -> new BadRequestException("Beacon ne može biti obrisan jer ne postoji"));

        Device device = beacon.getDevice();
        if (device != null) {
            device.setBeacon(null);
            deviceRepository.save(device);
        }

        beacon.setDevice(null);
        beacon = beaconRepository.save(beacon);

        beaconRepository.delete(beacon);
    }

    @Override
    public List<BeaconViewDto> findAllFreeBeacons() {
        return beaconMapper.entitysToViewsDto(beaconRepository.findAllByDeviceIsNull());
    }
}
