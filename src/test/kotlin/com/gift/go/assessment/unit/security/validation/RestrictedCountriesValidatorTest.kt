package com.gift.go.assessment.unit.security.validation

import com.gift.go.assessment.security.config.IPProperties
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.security.validation.IPValidationError
import com.gift.go.assessment.security.validation.RestrictedCountriesValidator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class RestrictedCountriesValidatorTest {

    companion object {
        private val ipProperties = mockk<IPProperties>()
        private val restrictedCountriesValidator: RestrictedCountriesValidator =
            RestrictedCountriesValidator(ipProperties)
    }

    @Test
    fun `Throw RestrictedCountryIPError on passing an ip of restricted country`() {
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
        every { ipProperties.restricted.countries } returns setOf("China","Spain","United States")

        // Then
        assertThrows<IPValidationError.RestrictedCountryIPError> {
            restrictedCountriesValidator.validate(ipInformation)
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
        every { ipProperties.restricted.countries } returns setOf("China","Spain","United States")

        // Then
        assertDoesNotThrow {
            restrictedCountriesValidator.validate(ipInformation)
        }
    }
}