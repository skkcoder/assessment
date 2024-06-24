package com.gift.go.assessment.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.gift.go.assessment.utils.getEntryFileStringContents
import com.gift.go.assessment.utils.getWireMockStubForIPValidScenario
import java.time.Duration
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
class FileProcessingIntegrationTest : BaseTests() {

    @Test
    fun `A file sent from valid ip should be processed, responds with OutcomeFile json and an audit entry is saved to db`() {
        // TODO migrate to rest assured for cleaner test

        // Given an input file with Header X-Forwarded-For
        val headers = HttpHeaders()
        headers.add("X-Forwarded-For", "149.250.252.66")
        val fileContents = getEntryFileStringContents()
        val byteArray = fileContents.toByteArray(Charsets.UTF_8)
        val resource = object : ByteArrayResource(byteArray) {
            override fun getFilename() = "file.txt"
        }
        val part: HttpEntity<ByteArrayResource> = HttpEntity(resource, HttpHeaders().apply {
            contentType = MediaType.APPLICATION_OCTET_STREAM
        })
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap<String, Any>().apply {
            add("file", part)
        }

        // when
        getWireMockStubForIPValidScenario()
        val request = RequestEntity.post("/api/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .headers(headers)
            .body(body)

        val response = restTemplate.exchange(request, String::class.java)

        // Then expect 200 OK with OutcomeFile.json
        assertEquals(HttpStatus.OK, response.statusCode)

        // assert file contents

        // use awaitility to assert db operation,

    }

}