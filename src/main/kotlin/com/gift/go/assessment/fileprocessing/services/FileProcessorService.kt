package com.gift.go.assessment.fileprocessing.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.gift.go.assessment.fileprocessing.processor.TextFileProcessor
import java.io.File
import org.springframework.stereotype.Service

@Service
class FileProcessorService(val objectMapper: ObjectMapper) {

    fun process(fileContent: ByteArray): File {
        // TODO -> Use temp directory and create the file in it.
        val tempFile: File = File.createTempFile("temp", ".txt")
        tempFile.writeBytes(fileContent)
        val entryFileContents = TextFileProcessor.processTextFile(tempFile)

        val jsonList = entryFileContents.map { entry ->
            mapOf(
                "name" to entry.name,
                "transport" to entry.transport,
                "topSpeed" to entry.topSpeed
            )
        }
        val jsonString = objectMapper.writeValueAsString(jsonList)
        val outcomeFile = File.createTempFile("OutcomeFile", ".json")
        outcomeFile.writeText(jsonString)
        return outcomeFile
    }
}