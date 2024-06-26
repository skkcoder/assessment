package com.gift.go.assessment.security.domain

sealed class IPResult

data class IPInformation(
    val isp: String,
    val org: String,
    val `as`: String,
    val country: String,
    val countryCode: String,
    val status: String
) :
    IPResult()

data class IPFail(val query: String, val message: String, val status: String) : IPResult()
