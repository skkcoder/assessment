package com.gift.go.assessment.fileprocessing.processor

import com.gift.go.assessment.fileprocessing.domain.EntryFileContent
import com.gift.go.assessment.fileprocessing.exceptions.TextFileProcessingError
import com.gift.go.assessment.fileprocessing.services.IPInformationService
import java.io.File
import java.util.UUID
import org.slf4j.LoggerFactory

class TextFileProcessor {

    private val logger = LoggerFactory.getLogger(IPInformationService::class.java)

    fun processTextFile(file: File): List<EntryFileContent> {
        return file.useLines {
            it
                .filter { line -> line.trim().isNotBlank() }
                .map { line -> processLine(line) }
                .toList()
        }
    }

    private fun processLine(line: String): EntryFileContent = try {
        val parts = line.split(LINE_SPLITTER)
        require(parts.size == 7) {
            "Invalid file received. Required parts 7, but line contain only ${parts.size}"
        }
        val entryId = UUID.fromString(parts[0])
        val id = parts[1]
        val name = parts[2]
        val likes = parts[3]
        val transport = parts[4]
        val avgSpeed = parts[5].toDouble()
        val topSpeed = parts[6].toDouble()
        EntryFileContent(entryId, id, name, likes, transport, avgSpeed, topSpeed)
    } catch (e: IllegalArgumentException) {
        throw TextFileProcessingError.InvalidTextFileContentError("Invalid file received - ${e.message}")
    } catch (e: Exception) {
        throw TextFileProcessingError.GenericError("Unable to process content - ${e.message}")
    }

    companion object {
        const val LINE_SPLITTER = "|"
    }
}