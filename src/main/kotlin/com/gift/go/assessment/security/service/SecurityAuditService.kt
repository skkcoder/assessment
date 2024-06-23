package com.gift.go.assessment.security.service

import com.gift.go.assessment.security.domain.SecurityAuditInformation
import org.springframework.stereotype.Service

@Service
class SecurityAuditService {

    suspend fun saveAudit(auditInformation: SecurityAuditInformation) {
        println("=------------${auditInformation}")
    }
}