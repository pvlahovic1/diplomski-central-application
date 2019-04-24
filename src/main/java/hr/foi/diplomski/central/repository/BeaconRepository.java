package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeaconRepository extends JpaRepository<Beacon, Long> {

    Optional<Beacon> findByUuidAndMajorAndMinor(String uuid, Integer major, Integer minor);

    List<Beacon> findAllByUser(User user);

    List<Beacon> findAllByUserAndDevicesIsEmpty(User user);

}
