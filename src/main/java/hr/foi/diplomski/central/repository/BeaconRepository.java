package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeaconRepository extends JpaRepository<Beacon, Integer> {

}
