package com.gift.go.assessment.integration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName

abstract class BaseTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    companion object {
        @Container
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:latest"))

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            Startables.deepStart(postgresContainer).join()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgresContainer.stop()
        }

        @DynamicPropertySource
        fun registerApplicationProperties(registry: DynamicPropertyRegistry) {
            // register postgres properties for the test
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }
}