package com.gift.go.assessment.fileprocessing.validation

import com.gift.go.assessment.fileprocessing.domain.IPFail
import com.gift.go.assessment.fileprocessing.domain.IPInformation
import com.gift.go.assessment.fileprocessing.domain.IPResult
import com.gift.go.assessment.fileprocessing.exceptions.IPValidationError
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class FileProcessorRequestValidator(
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
    @Value("\${ip.restricted.datacentres}") val restrictedDataCentres: String
) : IPValidator {
    private lateinit var restrictedDataCentreSet: Set<String>

    @PostConstruct
    fun init() {
        restrictedDataCentreSet = this.restrictedDataCentres.split(",").toSet()
    }

    override fun validate(ipInformation: IPInformation) {
        if (ipInformation.isp in restrictedDataCentreSet) {
            throw IPValidationError.RestrictedDataCenterIPError("Requests not allowed for requests originating from ${ipInformation.isp}")
        }
    }
}

@Component
class RestrictedCountriesValidator(
    @Value("\${ip.restricted.countries}") val restrictedCountries: String
) : IPValidator {
    private lateinit var restrictedCountrySet: Set<String>

    @PostConstruct
    fun init() {
        restrictedCountrySet = this.restrictedCountries.split(",").toSet()
    }

    override fun validate(ipInformation: IPInformation) {
        if (ipInformation.country in restrictedCountrySet) {
            throw IPValidationError.RestrictedCountryIPError("Requests not allowed for requests originating from ${ipInformation.country}")
        }
    }
}

