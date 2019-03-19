package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeaconRepository extends JpaRepository<Beacon, Integer> {

    Optional<Beacon> findByUuidAndMajorAndMinor(String uuid, Integer major, Integer minor);

}
