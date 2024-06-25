package com.skkcoder.assessment.unit.security.validation

import com.skkcoder.assessment.security.domain.IPFail
import com.skkcoder.assessment.security.domain.IPInformation
import com.skkcoder.assessment.security.validation.IPValidationError
import com.skkcoder.assessment.security.validation.IPValidations
import com.skkcoder.assessment.security.validation.IPValidator
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

class IPValidationsTest {
    private lateinit var validator: IPValidations
    private val mockIPValidator: IPValidator = Mockito.mock(IPValidator::class.java)

    @BeforeEach
    fun setUp() {
        validator = IPValidations(listOf(mockIPValidator))
    }

    @Test
    fun `should throw InvalidIPError when IPResult is IPFail`() = runTest {
        val ipFail = IPFail(query = "invalid query", message = "Invalid IP Address", status = "fail")

        val exception = assertThrows<IPValidationError.InvalidIPError> {
            validator.validate(ipFail)
        }

        assertEquals(ipFail.message, exception.message)
    }

    @Test
    fun `should call IPValidator when IPResult is IPInformation`() = runTest {
        val ipInformation = IPInformation(
            isp = "ISP",
            org = "ORG",
            `as` = "AS",
            country = "Country",
            countryCode = "CountryCode",
            status = "Status"
        )

        validator.validate(ipInformation)

        Mockito.verify(mockIPValidator, Mockito.times(1)).validate(ipInformation)
    }
}