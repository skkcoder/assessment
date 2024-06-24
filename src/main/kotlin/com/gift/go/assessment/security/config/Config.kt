package com.gift.go.assessment.security.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ConfigurationPropertiesScan
class Config(val ipProperties: IPProperties) {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient =
        builder
            .baseUrl(ipProperties.validation.apiUrl)
            .build()
}