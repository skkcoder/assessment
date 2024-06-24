package com.gift.go.assessment.security.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.gift.go.assessment.security.domain.IPFail
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.security.domain.IPResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class IPInformationService(val webClient: WebClient, val objectMapper: ObjectMapper) {

    private val logger = LoggerFactory.getLogger(IPInformationService::class.java)

    /**
     * The IP information provided by the remote API returns 200 OK regardless
     * of using a normal ip, or a reserved range or any other exceptional scenarios.
     * We won't count invalid ip strings because it is already being validated before.
     * TODO: Improve resilience - currently, this WebClient doesnt retry requests on failures
     * (e.g., HTTP 500)
     */
    suspend fun getIPInformation(ipAddress: String): IPResult {
        val response = webClient.get()
            .uri("/json/$ipAddress")
            .retrieve()
            .awaitBody<JsonNode>()
        logger.info("Successfully received response from IP API, status= ${response.get("status")?.asText()}")
        return if (response.get("status")?.asText() == "success") {
            objectMapper.treeToValue(response, IPInformation::class.java)
        } else {
            objectMapper.treeToValue(response, IPFail::class.java)
        }
    }
}