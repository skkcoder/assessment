package com.gift.go.assessment.fileprocessing.exceptions

sealed class TextFileProcessingError(message: String) : Exception(message) {
    class InvalidTextFileContentError(message: String) : TextFileProcessingError(message)
    class GenericError(message: String) : TextFileProcessingError(message)
}

sealed class IPValidationError(message: String) : Exception(message) {
    class InvalidIPError(message: String) : IPValidationError(message)
    class RestrictedCountryIPError(message: String) : IPValidationError(message)
    class RestrictedDataCenterIPError(message: String) : IPValidationError(message)
}