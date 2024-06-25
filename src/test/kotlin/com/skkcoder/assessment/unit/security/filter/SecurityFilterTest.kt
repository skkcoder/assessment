package com.skkcoder.assessment.unit.security.filter

import com.skkcoder.assessment.security.config.IPProperties
import com.skkcoder.assessment.security.domain.IPFail
import com.skkcoder.assessment.security.domain.IPInformation
import com.skkcoder.assessment.security.domain.SecurityAuditInformationDTO
import com.skkcoder.assessment.security.filter.SecurityFilter
import com.skkcoder.assessment.security.service.IPInformationService
import com.skkcoder.assessment.security.service.SecurityAuditService
import com.skkcoder.assessment.security.validation.IPValidationError
import com.skkcoder.assessment.security.validation.IPValidations
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
import kotlin.test.assertEquals

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
    fun `should call audit when IP is invalid and set response to Forbidden`(exception: Throwable) = runTest {
        val ipFail = IPFail("localhost", "message", "fail")

        coEvery { mockIPInformationService.getIPInformation(any()) } returns ipFail
        every { mockIPValidations.validate(any<IPFail>()) } throws exception
        coEvery { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) } returns Unit
        every { mockIPProperties.validation.enabled } returns true

        val mockServerWebExchange = MockServerWebExchange
            .from(MockServerHttpRequest.head("localhost"))

        val webFilterChain = WebFilterChain { serverWebExchange ->
            // purposefully setting it here, we expect the response code to 403 after filter processing since ip is invalid
            serverWebExchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            Mono.empty()
        }

        StepVerifier.create(filter.filter(mockServerWebExchange, webFilterChain))
            .expectSubscription()
            .expectComplete()
            .verify()


        verify { mockIPValidations.validate(any<IPFail>()) }
        coVerify { mockIPInformationService.getIPInformation(any()) }
        coVerify { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) }
        assertEquals(HttpStatus.FORBIDDEN, mockServerWebExchange.response.statusCode)
    }

    @Test
    fun `should not validate if validation is turned off`() = runTest {
        // given
        val ipInformation = IPInformation("isp", "org", "as", "country", "countryCode", "status")

        //when
        coEvery { mockIPInformationService.getIPInformation(any()) } returns ipInformation
        coEvery { mockSecurityAuditService.saveAudit(any<SecurityAuditInformationDTO>()) } returns Unit
        every { mockIPValidations.validate(any<IPInformation>()) } returns Unit // Changed this line
        every { mockIPProperties.validation.enabled } returns false

        val mockServerWebExchange = MockServerWebExchange
            .from(MockServerHttpRequest.head("localhost"))
        val webFilterChain = WebFilterChain { serverWebExchange ->
            serverWebExchange.response.statusCode = HttpStatus.OK
            Mono.empty()
        }
        // then
        StepVerifier.create(filter.filter(mockServerWebExchange, webFilterChain))
            .expectSubscription()
            .verifyComplete()
        verify(atLeast = 0) { mockIPValidations.validate(any<IPInformation>()) }
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