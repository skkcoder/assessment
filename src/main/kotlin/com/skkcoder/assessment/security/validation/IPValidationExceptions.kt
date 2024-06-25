package com.skkcoder.assessment.security.validation

sealed class IPValidationError(message: String) : Exception(message) {
    class InvalidIPError(message: String) : IPValidationError(message)
    class RestrictedCountryIPError(message: String) : IPValidationError(message)
    class RestrictedDataCenterIPError(message: String) : IPValidationError(message)
}
