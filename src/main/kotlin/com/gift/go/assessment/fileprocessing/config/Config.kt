package com.gift.go.assessment.fileprocessing.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class Config {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl("http://ip-api.com/") // TODO make it configurable
            .build()

    fun objectMapper() = jacksonObjectMapper().apply {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

}