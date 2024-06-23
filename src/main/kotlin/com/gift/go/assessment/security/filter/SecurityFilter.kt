package com.gift.go.assessment.security.filter

import com.gift.go.assessment.security.domain.IPResult
import com.gift.go.assessment.security.service.IPInformationService
import com.gift.go.assessment.security.service.SecurityAuditService
import com.gift.go.assessment.security.validation.IPValidations
import java.time.LocalDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class SecurityFilter(
    val ipInformationService: IPInformationService,
    val ipValidations: IPValidations,
    val auditService: SecurityAuditService
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val ip = extractIpInformation(exchange)
        val requestStartedAt = LocalDateTime.now()

        return mono {
            val ipResult = ipInformationService.getIPInformation(ip)
            addAttributesToExchange(exchange, ipResult, ip, requestStartedAt)
            ipValidations.validate(ipResult)
        }.then(chain.filter(exchange))
            .doAfterTerminate {
                CoroutineScope(Dispatchers.IO).launch {
                    val securityAuditInformation = exchange.toSecurityAudit()
                    auditService.saveAudit(securityAuditInformation)
                }
            }
    }

    private fun addAttributesToExchange(
        exchange: ServerWebExchange,
        ipResult: IPResult,
        ip: String,
        requestStartedAt: LocalDateTime
    ) {
        val attributes: MutableMap<String, Any> = exchange.attributes
        // Add new attributes to the existing attributes of ServerWebExchange
        attributes["IP_INFO"] = ipResult
        attributes["REQUEST_START_TIME"] = requestStartedAt
        attributes["REQUEST_IP"] = ip
        attributes["REQUEST_INSTANT"] = System.nanoTime()
    }

    private fun extractIpInformation(exchange: ServerWebExchange): String {
        val forwardedIp = exchange.request.headers.getFirst("X-Forwarded-For")?.split(",")?.get(0)

        return when {
            !forwardedIp.isNullOrEmpty() -> forwardedIp
            exchange.request.remoteAddress != null -> exchange.request.remoteAddress!!.address.hostAddress
            else -> "IP address couldn't be resolved"
        }
    }
}
