package com.skkcoder.assessment.security.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ip")
data class IPProperties(
    val validation: ValidationProperties,
    val restricted: RestrictedProperties
) {
    data class ValidationProperties(
        val enabled: Boolean,
        val apiUrl: String
    )

    data class RestrictedProperties(
        val datacentres: Set<String>,
        val countries: Set<String>
    )
}