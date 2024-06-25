package com.skkcoder.assessment.fileprocessing.api

import com.skkcoder.assessment.fileprocessing.services.FileProcessorService
import com.skkcoder.assessment.fileprocessing.utils.toByteArray
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
class FileProcessorController(
    val fileProcessorService: FileProcessorService,
) {
    private val logger = LoggerFactory.getLogger(FileProcessorController::class.java)

    @PostMapping("/api/files")
    suspend fun processFile(
        @RequestPart("file") filePart: FilePart,
        @RequestHeader headers: HttpHeaders?,
        serverWebExchange: ServerWebExchange
    ): ResponseEntity<Resource> {
        logger.debug("Processing file started. Request origin: {}", serverWebExchange.getAttribute<Any>("IP_INFO"))
        val outcomeFile = fileProcessorService.process(filePart.toByteArray())
        // return Resource containing this outcomeFile
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${outcomeFile.name}\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(FileSystemResource(outcomeFile))
    }

}
