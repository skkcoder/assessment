package com.gift.go.assessment.fileprocessing.exceptions

sealed class TextFileProcessingError(message: String) : Exception(message) {
    class InvalidTextFileContentError(message: String) : TextFileProcessingError(message)
    class GenericError(message: String) : TextFileProcessingError(message)
}

