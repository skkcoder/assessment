package com.gift.go.assessment.unit.security.service

import com.gift.go.assessment.security.domain.SecurityAuditInformationDTO
import com.gift.go.assessment.security.domain.mapToTable
import com.gift.go.assessment.security.repositories.SecurityAuditRepository
import com.gift.go.assessment.security.service.SecurityAuditService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

class SecurityAuditServiceTest {
    private val securityAuditRepository: SecurityAuditRepository = mockk()
    private val service = SecurityAuditService(securityAuditRepository)

    @Test
    fun `saveAudit calls repository's save method`() = runTest {
        // given
        val dto = SecurityAuditInformationDTO(
            "111.200.252.66",
            "China Unicom Beijing Province Network",
            "CN",
            "/api/files",
            LocalDateTime.now(),
            17944000,
            500
        )

        val entity = mapToTable(dto)
        // when
        coEvery { securityAuditRepository.save(entity) } returns entity
        service.saveAudit(dto)

        // then
        coVerify { securityAuditRepository.save(entity) }
    }
}