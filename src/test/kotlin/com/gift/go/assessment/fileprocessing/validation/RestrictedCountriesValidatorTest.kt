package com.gift.go.assessment.fileprocessing.validation

import com.gift.go.assessment.fileprocessing.domain.IPInformation
import com.gift.go.assessment.fileprocessing.exceptions.IPValidationError
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class RestrictedCountriesValidatorTest {

    companion object {
        private val restrictedCountriesValidator: RestrictedCountriesValidator =
            RestrictedCountriesValidator("China,Spain,United States")

        @JvmStatic
        @BeforeAll
        fun setup() {
            restrictedCountriesValidator.init()
        }
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
            status = "success",
            ip = "192.168.1.2"
        )

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
            status = "success",
            ip = "192.168.1.2"
        )

        // Then
        assertDoesNotThrow {
            restrictedCountriesValidator.validate(ipInformation)
        }
    }
}