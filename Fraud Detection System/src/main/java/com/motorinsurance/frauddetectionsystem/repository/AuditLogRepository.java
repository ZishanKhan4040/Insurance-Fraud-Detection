
package com.motorinsurance.frauddetectionsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motorinsurance.frauddetectionsystem.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findByUser_UserId(Integer userId);

    List<AuditLog> findByEntityTypeAndEntityId(
            String entityType, Integer entityId);
}
