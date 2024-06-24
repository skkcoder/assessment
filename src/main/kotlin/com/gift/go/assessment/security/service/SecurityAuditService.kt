package com.gift.go.assessment.security.service

import com.gift.go.assessment.security.domain.SecurityAuditInformationDTO
import com.gift.go.assessment.security.domain.mapToTable
import com.gift.go.assessment.security.repositories.SecurityAuditRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SecurityAuditService(val securityAuditRepository: SecurityAuditRepository) {

    private val logger = LoggerFactory.getLogger(SecurityAuditService::class.java)

    @Transactional
    suspend fun saveAudit(securityAuditInformationDTO: SecurityAuditInformationDTO) {
        val securityAuditInformation =  mapToTable(securityAuditInformationDTO)
        // TODO could have used r2dbc here, Currently running the blocking operation in its own context instead of blocking the reactor thread.
        withContext(Dispatchers.IO) {
            val audit = securityAuditRepository.save(securityAuditInformation)
            logger.info("Successfully logged audit in to the table - ${audit.requestId}")
        }
    }
}