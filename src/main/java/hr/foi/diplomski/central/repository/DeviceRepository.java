package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Device;
import hr.foi.diplomski.central.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAllByUserAndBeaconIsNull(User user);

    List<Device> findAllByUser(User user);

}
