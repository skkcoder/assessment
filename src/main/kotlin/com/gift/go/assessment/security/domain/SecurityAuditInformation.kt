package com.gift.go.assessment.security.domain

import java.time.LocalDateTime
import java.util.UUID

data class SecurityAuditInformation(
    val requestId: UUID,
    val requestIp: String,
    val requestIpProvider: String? = null,
    val requestCountryCode: String? = null,
    val requestUri: String,
    val requestStart: LocalDateTime,
    val timeLapsed: Long,
    val responseCode: Int
)