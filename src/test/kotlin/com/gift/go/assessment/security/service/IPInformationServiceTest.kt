package com.gift.go.assessment.security.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.gift.go.assessment.security.domain.IPFail
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.utils.getMockIPInformationForAws
import com.gift.go.assessment.utils.getMockIPInformationForAzure
import com.gift.go.assessment.utils.getMockIPInformationForGcp
import com.gift.go.assessment.utils.getReservedRangeIpInformation
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class IPInformationServiceTest {

    companion object {
        private val mockWebServer: MockWebServer = MockWebServer()
        lateinit var sut: IPInformationService

        @JvmStatic
        @BeforeAll
        fun setUp() {
            val webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build()
            val objectMapper = jacksonObjectMapper().apply {
                this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
            sut = IPInformationService(webClient, objectMapper)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            mockWebServer.shutdown()
        }

        @JvmStatic
        fun getIpData(): List<Arguments> = listOf(
            Arguments.of("13.66.143.220", getMockIPInformationForAzure()),
            Arguments.of("23.20.0.1", getMockIPInformationForAws()),
            Arguments.of("34.35.0.1", getMockIPInformationForGcp())
        )
    }

    @ParameterizedTest(name = "Requesting IP Information for a valid ip")
    @MethodSource("getIpData")
    fun `Given a valid IP, When requesting information, Then return IPInformation`(
        inputIp: String,
        ipInformation: String
    ) = runTest {

        // When
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(ipInformation)
        )
        val resultIpInformation = sut.getIPInformation(inputIp)
        // Then
        require(resultIpInformation is IPInformation) {
            "Expected IPInformation, but got something unexpected ${resultIpInformation.javaClass.name}"
        }
        assertEquals("success", resultIpInformation.status)
        assertNotNull(resultIpInformation.isp)
        assertNotNull(resultIpInformation.country)
        assertNotNull(resultIpInformation.org)
        assertNotNull(resultIpInformation.`as`)
    }

    @Test
    fun `Given a reserved range IP, When requesting information, Then return failure response`() = runTest {

        // When
        mockWebServer.enqueue(
            MockResponse().setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(getReservedRangeIpInformation())
        )
        val resultIpInformation = sut.getIPInformation("192.0.2.1")
        require(resultIpInformation is IPFail) {
            "Expected IPFail, but got something unexpected ${resultIpInformation.javaClass.name}"
        }
        assertEquals("fail", resultIpInformation.status)
    }
}