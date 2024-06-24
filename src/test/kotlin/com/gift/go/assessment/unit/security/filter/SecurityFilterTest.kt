package com.gift.go.assessment.unit.security.filter

import com.gift.go.assessment.security.config.IPProperties
import com.gift.go.assessment.security.domain.IPFail
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.security.domain.SecurityAuditInformationDTO
import com.gift.go.assessment.security.filter.SecurityFilter
import com.gift.go.assessment.security.service.IPInformationService
import com.gift.go.assessment.security.service.SecurityAuditService
import com.gift.go.assessment.security.validation.IPValidationError
import com.gift.go.assessment.security.validation.IPValidations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.stream.Stream
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class SecurityFilterTest {

    private val mockIPInformationService: IPInformationService = mockk<IPInformationService>()
    private val mockIPValidations: IPValidations = mockk<IPValidations>()
    private val mockSecurityAuditService: SecurityAuditService = mockk<SecurityAuditService>()
    private val mockIPProperties = mockk<IPProperties>()

    private val filter =
        SecurityFilter(mockIPInformationService, mockIPValidations, mockSecurityAuditService, mockIPProperties)

    @Test
    fun `should filter successfully and call audit with valid IP`() = runTest {
        val ipInformation = IPInformation("isp", "org", "as", "country", "countryCode", "status")
        coEvery { mockIPInformationService.getIPInformation(any()) } returns ipInformation
        coEvery { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) } returns Unit
        every { mockIPValidations.validate(any<IPInformation>()) } returns Unit // Changed this line
        every { mockIPProperties.validation.enabled } returns true

        val mockServerWebExchange = MockServerWebExchange
            .from(MockServerHttpRequest.head("localhost"))
        val webFilterChain = WebFilterChain { serverWebExchange ->
            serverWebExchange.response.statusCode = HttpStatus.OK
            Mono.empty()
        }

        StepVerifier.create(filter.filter(mockServerWebExchange, webFilterChain))
            .expectSubscription()
            .verifyComplete()
        verify { mockIPValidations.validate(any<IPInformation>()) }
        coVerify { mockIPInformationService.getIPInformation(any()) }
        coVerify { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) }
    }


    @ParameterizedTest(name = "should call audit when IP is {0}")
    @MethodSource("provideExceptions")
    fun `should call audit when IP is invalid`(exception: Throwable) = runTest {
        val ipFail = IPFail("localhost", "message", "fail")
        coEvery { mockIPInformationService.getIPInformation(any()) } returns ipFail
        every { mockIPValidations.validate(any<IPFail>()) } throws exception
        coEvery { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) } returns Unit
        every { mockIPProperties.validation.enabled } returns true

        val mockServerWebExchange = MockServerWebExchange
            .from(MockServerHttpRequest.head("localhost"))

        val webFilterChain = WebFilterChain { serverWebExchange ->
            serverWebExchange.response.statusCode = HttpStatus.FORBIDDEN
            Mono.empty()
        }

        StepVerifier.create(filter.filter(mockServerWebExchange, webFilterChain))
            .expectSubscription()
            .expectComplete()
            .verify()

        verify { mockIPValidations.validate(any<IPFail>()) }
        coVerify { mockIPInformationService.getIPInformation(any()) }
        coVerify { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) }
    }

    companion object {
        @JvmStatic
        fun provideExceptions(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(IPValidationError.InvalidIPError("Invalid IP error")),
                Arguments.of(IPValidationError.RestrictedCountryIPError("Restricted country IP error")),
                Arguments.of(IPValidationError.RestrictedDataCenterIPError("Restricted data center error"))
            )
        }
    }
}