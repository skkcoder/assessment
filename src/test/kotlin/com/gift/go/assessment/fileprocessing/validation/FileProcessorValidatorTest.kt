package com.gift.go.assessment.fileprocessing.validation

import com.gift.go.assessment.fileprocessing.domain.IPFail
import com.gift.go.assessment.fileprocessing.domain.IPInformation
import com.gift.go.assessment.fileprocessing.exceptions.IPValidationError
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

class FileProcessorRequestValidatorTest {
    private lateinit var validator: FileProcessorRequestValidator
    private val mockIPValidator: IPValidator = Mockito.mock(IPValidator::class.java)

    @BeforeEach
    fun setUp() {
        validator = FileProcessorRequestValidator(listOf(mockIPValidator))
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
            status = "Status",
            ip = "0.0.0.0"
        )

        validator.validate(ipInformation)

        Mockito.verify(mockIPValidator, Mockito.times(1)).validate(ipInformation)
    }
}