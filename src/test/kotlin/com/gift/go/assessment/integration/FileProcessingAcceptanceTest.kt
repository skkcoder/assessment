package com.gift.go.assessment.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
class FileProcessingAcceptanceTest : BaseTests() {

    @Test
    fun `A file sent from valid ip should be processed, responds with Output file and an audit entry is saved`() {

    }

}