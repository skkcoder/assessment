package com.skkcoder.assessment.unit.security.validation

import com.skkcoder.assessment.security.config.IPProperties
import com.skkcoder.assessment.security.domain.IPInformation
import com.skkcoder.assessment.security.validation.IPValidationError
import com.skkcoder.assessment.security.validation.RestrictedDataCentresValidator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class RestrictedDataCentresValidatorTest {

    companion object {
        private val ipProperties = mockk<IPProperties>()
        private val restrictedDataCentresValidator: RestrictedDataCentresValidator =
            RestrictedDataCentresValidator(ipProperties)
    }

    @Test
    fun `Throw RestrictedDataCenterIPError on passing an ip of restricted data centre`() {
        // given
        val ipInformation = IPInformation(
            isp = "Amazon.com",
            org = "AWS EC2 (us-east-1)",
            `as` = "AS14618 Amazon.com, Inc.",
            country = "United States",
            countryCode = "US",
            status = "success"
        )

        // when
        every { ipProperties.restricted.datacentres } returns setOf("Microsoft Corporation","Amazon.com","Google LLC")

        // Then
        assertThrows<IPValidationError.RestrictedDataCenterIPError> {
            restrictedDataCentresValidator.validate(
                ipInformation
            )
        }
    }

    @Test
    fun `RestrictedDataCenterIPError on passing an ip of valid data centre should pass`() {
        // given
        val ipInformation = IPInformation(
            isp = "Oracle.com",
            org = "Oracle Cloud",
            `as` = "AS14618 Amazon.com, Inc.",
            country = "France",
            countryCode = "FR",
            status = "success"
        )

        // when
        every { ipProperties.restricted.datacentres } returns setOf("Microsoft Corporation","Amazon.com","Google LLC")

        // Then
        assertDoesNotThrow {
            restrictedDataCentresValidator.validate(ipInformation)
        }
    }
}