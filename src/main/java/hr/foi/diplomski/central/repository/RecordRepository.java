package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.Sensor;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.model.record.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, RecordId> {

    Optional<Record> findFirstByRecordId_BeaconOrderByRecordId_RecordDateDesc(Beacon beacon);

    void deleteAllByRecordId_Sensor(List<Sensor> sensor);

    void deleteAllByRecordId_Beacon(Beacon beacon);

    Optional<Record> findFirstByRecordId_BeaconAndRecordId_RecordDateBetweenOrderByDistanceAsc(Beacon beacon,
                                                                                               LocalDateTime validFrom,
                                                                                               LocalDateTime validTo);
}
