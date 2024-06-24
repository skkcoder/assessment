package com.gift.go.assessment.fileprocessing.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.gift.go.assessment.fileprocessing.domain.EntryFileContent
import com.gift.go.assessment.fileprocessing.processor.TextFileProcessor
import java.io.File
import org.springframework.stereotype.Service

@Service
class FileProcessorService(
    val objectMapper: ObjectMapper,
    val fileService: FileService,
    val textProcessor: TextFileProcessor
) {

    fun process(fileContent: ByteArray): File {
        val entryFileContents = textProcessor.processTextFile(fileContent)
        val jsonString = processEntryFileContents(entryFileContents)
        val outcomeFile = fileService.createTempFile("OutcomeFile", ".json", jsonString)
        return outcomeFile
    }

    private fun processEntryFileContents(entryFileContents: List<EntryFileContent>): String {
        val jsonList = entryFileContents.map { entry ->
            mapOf(
                "name" to entry.name,
                "transport" to entry.transport,
                "topSpeed" to entry.topSpeed
            )
        }
        val jsonString = objectMapper.writeValueAsString(jsonList)
        return jsonString
    }
}