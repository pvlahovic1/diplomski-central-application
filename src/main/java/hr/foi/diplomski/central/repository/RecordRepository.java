package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.Beacon;
import hr.foi.diplomski.central.model.record.Record;
import hr.foi.diplomski.central.model.record.RecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, RecordId> {

    Optional<Record> findFirstByRecordId_BeaconOrderByRecordId_RecordDateDesc(Beacon beacon);

}
