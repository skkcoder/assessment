package com.gift.go.assessment.fileprocessing.services

import java.io.File
import org.springframework.stereotype.Component

interface FileService {
    fun createTempFile(prefix: String, suffix: String, content: ByteArray): File
    fun createTempFile(prefix: String, suffix: String, content: String): File
}

@Component
class DefaultFileService : FileService {
    override fun createTempFile(prefix: String, suffix: String, content: ByteArray): File {
        val tempFile: File = File.createTempFile(prefix, suffix)
        tempFile.writeBytes(content)
        return tempFile
    }

    override fun createTempFile(prefix: String, suffix: String, content: String): File {
        val tempFile: File = File.createTempFile(prefix, suffix)
        tempFile.writeText(content)
        return tempFile
    }
}