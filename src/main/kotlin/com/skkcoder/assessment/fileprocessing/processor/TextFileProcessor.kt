package com.skkcoder.assessment.fileprocessing.processor

import com.skkcoder.assessment.fileprocessing.domain.EntryFileContent
import com.skkcoder.assessment.fileprocessing.exceptions.TextFileProcessingError
import java.util.*
import org.springframework.stereotype.Component

@Component
class TextFileProcessor {

    companion object {
        private const val LINE_SPLITTER = "|"
    }

    fun processTextFile(byteArray: ByteArray): List<EntryFileContent> {
        return String(byteArray).split("\n")
            .filter { line -> line.trim().isNotBlank() }
            .map { line ->
                processLine(line)
            }.toList()
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

}