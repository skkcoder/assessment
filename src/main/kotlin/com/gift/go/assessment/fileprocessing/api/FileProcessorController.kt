package com.gift.go.assessment.fileprocessing.api

import java.net.http.HttpHeaders
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileProcessorController {

    @PostMapping("/api/files")
    suspend fun processFile(
        @RequestPart("file") file: MultipartFile,
        @RequestHeader headers: HttpHeaders?
    ): ResponseEntity<Resource> {
        val jsonData = """
            "Name": "John Smith",
            "Transport": "Rides A Bike",
            "Top Speed": "12.1"
        """.trimIndent().toByteArray()
        val resource = ByteArrayResource(jsonData)
        // Prepare headers
        val responseHeaders = org.springframework.http.HttpHeaders()
        responseHeaders.contentType = MediaType.APPLICATION_JSON
        responseHeaders.contentLength = jsonData.size.toLong()
        // Return response entity
        return ResponseEntity.ok()
            .headers(responseHeaders)
            .body(resource)
    }

}