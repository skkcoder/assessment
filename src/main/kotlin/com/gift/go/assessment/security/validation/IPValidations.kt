package com.gift.go.assessment.security.validation

import com.gift.go.assessment.security.config.IPProperties
import com.gift.go.assessment.security.domain.IPFail
import com.gift.go.assessment.security.domain.IPInformation
import com.gift.go.assessment.security.domain.IPResult
import org.springframework.stereotype.Component

@Component
class IPValidations(
    val ipValidators: List<IPValidator>
) {
    fun validate(ipResult: IPResult) {
        when (ipResult) {
            is IPFail -> throw IPValidationError.InvalidIPError(ipResult.message)
            is IPInformation -> {
                ipValidators.forEach { validator ->
                    validator.validate(ipResult)
                }
            }
        }
    }
}

interface IPValidator {
    fun validate(ipInformation: IPInformation)
}

@Component
class RestrictedDataCentresValidator(
    val ipProperties: IPProperties
) : IPValidator {
    override fun validate(ipInformation: IPInformation) {
        if (ipInformation.isp in ipProperties.restricted.datacentres) {
            throw IPValidationError.RestrictedDataCenterIPError("Requests not allowed for requests originating from ${ipInformation.isp}")
        }
    }
}

@Component
class RestrictedCountriesValidator(
    val ipProperties: IPProperties
) : IPValidator {

    override fun validate(ipInformation: IPInformation) {
        if (ipInformation.country in ipProperties.restricted.countries) {
            throw IPValidationError.RestrictedCountryIPError("Requests not allowed for requests originating from ${ipInformation.country}")
        }
    }
}

