package com.gift.go.assessment.security.domain

import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

data class SecurityAuditInformationDTO(
    val requestIp: String,
    val requestIpProvider: String? = null,
    val requestCountryCode: String? = null,
    val requestUri: String,
    val requestStart: LocalDateTime,
    val timeLapsed: Long,
    val responseCode: Int
)

@Table("assessment")
data class SecurityAuditInformation(
    @Id
    var requestId: UUID? = null,
    var requestIp: String? = null,
    var requestIpProvider: String? = null,
    var requestCountryCode: String? = null,
    var requestUri: String? = null,
    var requestStart: LocalDateTime? = null,
    var timeLapsed: Long? = null,
    var responseCode: Int? = null
)

/**
 * TODO - use a mapper
 */
fun mapToTable(dto: SecurityAuditInformationDTO) = SecurityAuditInformation().apply {
    requestIp = dto.requestIp
    requestIpProvider = dto.requestIpProvider
    requestCountryCode = dto.requestCountryCode
    requestUri = dto.requestUri
    requestStart = dto.requestStart
    timeLapsed = dto.timeLapsed
    responseCode = dto.responseCode
}