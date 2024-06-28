package com.skkcoder.assessment.unit.security.service

import com.skkcoder.assessment.security.domain.SecurityAuditInformation
import com.skkcoder.assessment.security.domain.SecurityAuditInformationDTO
import com.skkcoder.assessment.security.domain.SecurityAuditInformationMapper
import com.skkcoder.assessment.security.domain.mapToTable
import com.skkcoder.assessment.security.repositories.SecurityAuditRepository
import com.skkcoder.assessment.security.service.SecurityAuditService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test

class SecurityAuditServiceTest {
    private val securityAuditRepository: SecurityAuditRepository = mockk()
    private val mapper: SecurityAuditInformationMapper = mockk()
    private val service = SecurityAuditService(securityAuditRepository, mapper)

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


        // when
        every { mapper.mapToSecurityAuditInformation(dto) } returns SecurityAuditInformation()
        coEvery { securityAuditRepository.save(any<SecurityAuditInformation>()) } returns SecurityAuditInformation()
        service.saveAudit(dto)

        // then
        coVerify { securityAuditRepository.save(any<SecurityAuditInformation>()) }
    }
}