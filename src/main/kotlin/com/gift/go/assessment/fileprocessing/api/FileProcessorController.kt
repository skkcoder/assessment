package com.gift.go.assessment.fileprocessing.api

import com.gift.go.assessment.fileprocessing.services.FileProcessorService
import com.gift.go.assessment.fileprocessing.services.IPInformationService
import com.gift.go.assessment.fileprocessing.validation.FileProcessorRequestValidator
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileProcessorController(
    val ipInformationService: IPInformationService,
    val fileProcessorService: FileProcessorService,
    val fileProcessorRequestValidator: FileProcessorRequestValidator
) {

    @PostMapping("/api/files")
    suspend fun processFile(
        @RequestPart("file") file: MultipartFile,
        @RequestHeader headers: HttpHeaders?,
        request: ServerHttpRequest
    ): ResponseEntity<Resource> {
        // get ip information
        val ipAddress = extractIPAddress(headers, request)
        val ipInformation = ipInformationService.getIPInformation(ipAddress)
        fileProcessorRequestValidator.validate(ipInformation)
        val outcomeFile = fileProcessorService.process(file.bytes)
        // return Resource containing this outcomeFile
        val resource: Resource = FileSystemResource(outcomeFile)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${outcomeFile.name}\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

    private fun extractIPAddress(headers: HttpHeaders?, request: ServerHttpRequest): String {
        // extract ip address
        return ""
    }

}