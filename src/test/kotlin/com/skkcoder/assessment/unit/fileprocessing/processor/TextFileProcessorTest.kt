package com.skkcoder.assessment.unit.fileprocessing.processor

import com.skkcoder.assessment.fileprocessing.exceptions.TextFileProcessingError
import com.skkcoder.assessment.fileprocessing.processor.TextFileProcessor
import com.skkcoder.assessment.utils.getEntryFileContentsProcessed
import com.skkcoder.assessment.utils.getEntryFileInvalidContentFormat
import com.skkcoder.assessment.utils.getEntryFileStringContents
import java.io.File
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class TextFileProcessorTest {

    @Test
    fun `EntryFileContents text file is correctly processed`() {
        // Given
        val contents = getEntryFileStringContents()
        val file: File = File.createTempFile("test", ".txt")
        file.writeText(contents)

        // when
        val entryFileContents = TextFileProcessor().processTextFile(contents.toByteArray())

        // Then
        assertEquals(3, entryFileContents.size)
        assertEquals(getEntryFileContentsProcessed(), entryFileContents)
    }

    @Test
    fun `EntryFileContents text file with invalid content format throws InvalidTextFileContentError exception`() {
        // Given
        val contents = getEntryFileInvalidContentFormat()
        val file: File = File.createTempFile("test", ".txt")
        file.writeText(contents)

        // Then
        assertThrows<TextFileProcessingError.InvalidTextFileContentError> { TextFileProcessor().processTextFile(contents.toByteArray()) }
    }

}