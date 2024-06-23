package com.gift.go.assessment.fileprocessing.api

import com.gift.go.assessment.fileprocessing.services.FileProcessorService
import java.io.InputStream
import java.io.SequenceInputStream
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.withContext
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
class FileProcessorController(
    val fileProcessorService: FileProcessorService,
) {

    @PostMapping("/api/files")
    suspend fun processFile(
        @RequestPart("file") filePart: FilePart,
        @RequestHeader headers: HttpHeaders?
    ): ResponseEntity<Resource> {

        val outcomeFile = fileProcessorService.process(withContext(Dispatchers.IO) {
            filePart.asInputStream().readAllBytes()
        })
        // return Resource containing this outcomeFile
        val resource: Resource = FileSystemResource(outcomeFile)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${outcomeFile.name}\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

}

suspend fun Part.asInputStream(): InputStream =
    SequenceInputStream(
        Collections.enumeration(content().asFlow().map { it.asInputStream() }.toList())
    )