
package com.motorinsurance.frauddetectionsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.entity.AuditLog;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.AuditLogRepository;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditService(
            AuditLogRepository auditLogRepository,
            UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AuditLog logAction(
            Integer userId,
            String action,
            String entityType,
            Integer entityId,
            String details) {

        AuditLog log = new AuditLog();

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("User not found"));
            log.setUser(user);
        }

        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);

        return auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUserId(Integer userId) {
        return auditLogRepository.findByUser_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByEntity(
            String entityType, Integer entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(
                entityType, entityId);
    }
}
