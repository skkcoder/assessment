package com.gift.go.assessment.integration

import com.gift.go.assessment.utils.getEntryFileInvalidContentFormat
import com.gift.go.assessment.utils.getEntryFileStringContents
import com.gift.go.assessment.utils.getOutputFileContents
import com.gift.go.assessment.utils.getWireMockStubForIPInValidScenario
import com.gift.go.assessment.utils.getWireMockStubForIPValidScenario
import java.time.Duration
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
class FileProcessingIntegrationTest : BaseTests() {

    @BeforeEach
    fun setUpCleanState() {
        securityAuditRepository.deleteAll()
    }

    @Test
    fun `A file sent from valid ip should be processed, responds with OutcomeFile json and an audit entry is saved to db`() {
        // TODO migrate to rest assured for cleaner test

        // given
        val (headers, body: MultiValueMap<String, Any>) = prepareInput("149.250.252.66")

        // when
        getWireMockStubForIPValidScenario()
        val request = RequestEntity.post("/api/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .headers(headers)
            .body(body)
        val response = restTemplate.exchange(request, ByteArrayResource::class.java)

        // then
        assertEquals(HttpStatus.OK, response.statusCode)
        JSONAssert.assertEquals(getOutputFileContents(),  String(getResponseContents(response)), true)

        Awaitility.await().atMost(Duration.ofSeconds(1))
            .untilAsserted {
                val audits = securityAuditRepository.findAll().toList()
                assertEquals(1, audits.size)
                assertEquals("149.250.252.66", audits.first().requestIp)
                assertEquals(200, audits.first().responseCode)
            }
    }

    @Test
    fun `A file sent from invalid ip should be processed, responds with 403 Forbidden`() {
        // TODO migrate to rest assured for cleaner test

        // given
        val (headers, body: MultiValueMap<String, Any>) = prepareInput("149.251.252.66")

        // when
        getWireMockStubForIPInValidScenario()
        val request = RequestEntity.post("/api/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .headers(headers)
            .body(body)
        val response = restTemplate.exchange(request, String::class.java)

        // then
        assertEquals(HttpStatus.FORBIDDEN, response.statusCode)

        Awaitility.await().atMost(Duration.ofSeconds(1))
            .untilAsserted {
                val audits = securityAuditRepository.findAll().toList()
                assertEquals(1, audits.size)
                assertEquals("149.251.252.66", audits.first().requestIp)
                assertEquals(403, audits.first().responseCode)
            }

    }

    @Test
    fun `An invalid file sent from valid ip should be processed, responds with 500 Internal server error and an audit entry is saved to db`() {
        // This test should be actually 400 BadRequest by handling from ControllerAdvise (Not present in this project for now). Will tackle this later TIME!!

        // given
        val (headers, body: MultiValueMap<String, Any>) = prepareInvalidInput("149.250.252.66")

        // when
        getWireMockStubForIPValidScenario()
        val request = RequestEntity.post("/api/files")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .headers(headers)
            .body(body)
        val response = restTemplate.exchange(request, ByteArrayResource::class.java)

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)

        Awaitility.await().atMost(Duration.ofSeconds(1))
            .untilAsserted {
                val audits = securityAuditRepository.findAll().toList()
                assertEquals(1, audits.size)
                assertEquals("149.250.252.66", audits.first().requestIp)
                assertEquals(500, audits.first().responseCode)
            }
    }


    private fun getResponseContents(response: ResponseEntity<ByteArrayResource>): ByteArray {
        val responseBody: ByteArrayResource = response.body!!
        val fileContentAsByteArray: ByteArray = responseBody.byteArray
        return fileContentAsByteArray
    }

    private fun prepareInput(ip: String): Pair<HttpHeaders, MultiValueMap<String, Any>> {
        val headers = HttpHeaders()
        headers.add("X-Forwarded-For", ip)
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
        return Pair(headers, body)
    }

    private fun prepareInvalidInput(ip: String): Pair<HttpHeaders, MultiValueMap<String, Any>> {
        val headers = HttpHeaders()
        headers.add("X-Forwarded-For", ip)
        val fileContents = getEntryFileInvalidContentFormat()
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
        return Pair(headers, body)
    }

}