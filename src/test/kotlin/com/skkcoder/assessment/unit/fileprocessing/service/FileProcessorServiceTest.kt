package com.skkcoder.assessment.unit.fileprocessing.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.skkcoder.assessment.fileprocessing.processor.TextFileProcessor
import com.skkcoder.assessment.fileprocessing.services.FileProcessorService
import com.skkcoder.assessment.fileprocessing.services.FileService
import com.skkcoder.assessment.utils.getEntryFileContentsProcessed
import com.skkcoder.assessment.utils.getEntryFileStringContents
import com.skkcoder.assessment.utils.getOutputFileContents
import io.mockk.every
import io.mockk.mockk
import java.io.File
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class FileProcessorServiceTest {

    private val textFileProcessor = mockk<TextFileProcessor>()
    private val fileService = mockk<FileService>()
    private val objectMapper = mockk<ObjectMapper>()

    @Test
    fun `Test entry file contents processed to generate output file`() {
        val tempFile = File("temp.txt")
        val outFile = File("OutcomeFile.json")
        // given
        val entryFileBytes = getEntryFileStringContents().toByteArray()
        // when
        every { textFileProcessor.processTextFile(any()) } returns getEntryFileContentsProcessed()
        every { fileService.createTempFile(any(), any(), any<String>()) } returns outFile
        every { objectMapper.writeValueAsString(any()) } returns getOutputFileContents()
        val result = FileProcessorService(objectMapper, fileService, textFileProcessor).process(entryFileBytes)
        // then
        assertEquals(outFile, result)
    }
}
