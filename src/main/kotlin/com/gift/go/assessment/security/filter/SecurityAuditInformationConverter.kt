package com.gift.go.assessment.security.filter

import com.gift.go.assessment.security.domain.IPFail
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.security.domain.IPResult
import com.gift.go.assessment.security.domain.SecurityAuditInformationDTO
import java.lang.IllegalStateException
import java.time.LocalDateTime
import java.util.*
import org.springframework.web.server.ServerWebExchange

/**
 * Find a way to gracefully handle the illegal states
 */
fun ServerWebExchange.toSecurityAudit(): SecurityAuditInformationDTO {
    val ipResult = this.getAttribute<IPResult>("IP_INFO")
    val ip = this.getAttribute<String>("REQUEST_IP")
    val requestStartedAt = this.getAttribute<LocalDateTime>("REQUEST_START_TIME")
    val requestInstant =
        this.getAttribute<Long>("REQUEST_INSTANT") ?: throw IllegalStateException("Where is my request instant")
    val requestUri = this.request.uri.path
    val responseCode =
        this.response.statusCode?.value() ?: throw IllegalStateException("What happened to the response???")
    val timeLapsed = System.nanoTime() - requestInstant
    // FIXME -> Consider mappers
    return when (ipResult) {
        is IPInformation -> {
            SecurityAuditInformationDTO(
                requestIp = ip ?: "IP not found",
                requestIpProvider = ipResult.isp,
                requestCountryCode = ipResult.countryCode,
                requestUri = requestUri,
                requestStart = requestStartedAt ?: LocalDateTime.now(),
                timeLapsed = timeLapsed,
                responseCode = responseCode
            )
        }

        is IPFail -> {
            SecurityAuditInformationDTO(
                requestIp = ip ?: "IP not found",
                requestUri = requestUri,
                requestStart = requestStartedAt ?: LocalDateTime.now(),
                timeLapsed = timeLapsed,
                responseCode = responseCode
            )
        }
    }
}