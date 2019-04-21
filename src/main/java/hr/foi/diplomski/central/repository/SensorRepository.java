package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findBySensorId(String sensorId);

    List<Sensor> findByRoomId(Long roomId);

    List<Sensor> findAllByRoomIsNull();

}
