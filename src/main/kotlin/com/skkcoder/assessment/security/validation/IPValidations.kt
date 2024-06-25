package com.skkcoder.assessment.security.validation

import com.skkcoder.assessment.security.config.IPProperties
import com.skkcoder.assessment.security.domain.IPFail
import com.skkcoder.assessment.security.domain.IPInformation
import com.skkcoder.assessment.security.domain.IPResult
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

