package hr.foi.diplomski.central.service.beacon;

import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconDto;
import hr.foi.diplomski.central.controllers.api.beacons.dto.BeaconViewDto;
import hr.foi.diplomski.central.exceptions.BadRequestException;
import hr.foi.diplomski.central.mappers.beacon.BeaconMapper;
import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.User;
import hr.foi.diplomski.central.repository.BeaconRepository;
import hr.foi.diplomski.central.repository.DeviceRepository;
import hr.foi.diplomski.central.repository.RecordRepository;
import hr.foi.diplomski.central.service.centralaudit.CentralAuditService;
import hr.foi.diplomski.central.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BeaconServiceImpl implements BeaconService {

    private final BeaconRepository beaconRepository;
    private final DeviceRepository deviceRepository;
    private final RecordRepository recordRepository;
    private final CentralAuditService centralAuditService;
    private final BeaconMapper beaconMapper;
    private final UserService userService;

    @Override
    public List<BeaconDto> findAllBeacons() {
        return beaconMapper.entitysToDto(beaconRepository.findAllByUser(userService.findCurrentUser()));
    }

    @Override
    public BeaconDto findBeaconById(Long id) {
        Beacon beacon = beaconRepository.findById(id).orElseThrow(() -> new BadRequestException(
                String.format("Ne postoji beacon s id: %s", id)));

        User user = userService.findCurrentUser();

        if (user.equals(beacon.getUser())) {
            return beaconMapper.entityToDto(beacon);
        } else {
            throw new BadRequestException(String.format("Korisnik %s nije vlasnik beacona %s i ne može ga pregledati",
                    user.getUsername(), beacon.getId()));
        }

    }

    @Override
    public BeaconDto saveBeacon(BeaconDto beaconDto) {
        Beacon beacon = beaconMapper.dtoToEntity(beaconDto);
        User user = userService.findCurrentUser();

        if (beacon.getId() != null && beacon.getId() != 0) {
            if (user.equals(beacon.getUser())) {
                beacon = beaconRepository.save(beacon);
            } else {
                throw new BadRequestException(String.format("Korisnik %s nije vlasnik beacona %s i ne može ga uređivati",
                        user.getUsername(), beacon.getId()));
            }
        } else {
            beacon.setUser(user);
            beacon = beaconRepository.save(beacon);
        }

        return beaconMapper.entityToDto(beacon);
    }

    @Override
    @Transactional
    public void deleteBeaon(Long idBeacon) {
        Beacon beacon = beaconRepository.findById(idBeacon)
                .orElseThrow(() -> new BadRequestException(String
                        .format("Ne postoji beacon s id: %s", idBeacon)));

        User user = userService.findCurrentUser();

        if (user.equals(beacon.getUser())) {
            recordRepository.deleteAllByRecordId_Beacon(beacon);
            centralAuditService.deleteAuditByBeacon(beacon);

            beacon.getDevices().forEach(e -> e.setBeacon(null));
            beacon.getDevices().clear();

            beaconRepository.delete(beacon);
        } else {
            throw new BadRequestException(String.format("Korisnik %s nije vlasnik beacona %s i ne može ga obrisati",
                    user.getUsername(), beacon.getId()));
        }
    }

    @Override
    public List<BeaconViewDto> findAllFreeBeacons() {
        return beaconMapper.entitysToViewDtos(beaconRepository.findAllByUserAndDevicesIsEmpty(userService.findCurrentUser()));
    }
}
