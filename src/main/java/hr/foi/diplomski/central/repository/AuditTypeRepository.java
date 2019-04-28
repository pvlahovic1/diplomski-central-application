package hr.foi.diplomski.central.repository;

import hr.foi.diplomski.central.model.AuditType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditTypeRepository extends JpaRepository<AuditType, Long> {
}
