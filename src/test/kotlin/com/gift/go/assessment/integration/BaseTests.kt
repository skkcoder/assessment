package com.gift.go.assessment.integration

import com.gift.go.assessment.security.repositories.SecurityAuditRepository
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName


abstract class BaseTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var securityAuditRepository: SecurityAuditRepository

    companion object {
        @Container
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
        val wireMockServer: WireMockServer = WireMockServer(wireMockConfig().dynamicPort())

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            Startables.deepStart(postgresContainer).join()
            wireMockServer.start()
            // FIXME - check why dynamic property source is not working - TIME!!!
            System.setProperty("DB_URL", postgresContainer.jdbcUrl)
            System.setProperty("DB_USER", postgresContainer.username)
            System.setProperty("DB_PASSWORD", postgresContainer.password)
            System.setProperty("VALIDATION_URL", wireMockServer.baseUrl())
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresContainer.stop()
            wireMockServer.stop()
        }

    }

}