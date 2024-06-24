package com.gift.go.assessment.utils

import com.gift.go.assessment.fileprocessing.domain.EntryFileContent
import com.gift.go.assessment.integration.BaseTests.Companion.wireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import java.util.*
import org.springframework.http.MediaType

fun getMockIPInformationForAzure() = """
    {
      "query": "13.66.143.220",
      "status": "success",
      "country": "United States",
      "countryCode": "US",
      "region": "WA",
      "regionName": "Washington",
      "city": "Quincy",
      "zip": "98848",
      "lat": 47.233,
      "lon": -119.852,
      "timezone": "America/Los_Angeles",
      "isp": "Microsoft Corporation",
      "org": "Microsoft Azure Action Group (westus2)",
      "as": "AS8075 Microsoft Corporation"
    }
""".trimIndent()

fun getMockIPInformationForAws() = """
    {
      "query": "23.20.0.1",
      "status": "success",
      "country": "United States",
      "countryCode": "US",
      "region": "VA",
      "regionName": "Virginia",
      "city": "Ashburn",
      "zip": "20149",
      "lat": 39.0438,
      "lon": -77.4874,
      "timezone": "America/New_York",
      "isp": "Amazon.com",
      "org": "AWS EC2 (us-east-1)",
      "as": "AS14618 Amazon.com, Inc."
    }
""".trimIndent()

fun getMockIPInformationForGcp() = """
    {
      "query": "34.35.0.1",
      "status": "success",
      "country": "South Africa",
      "countryCode": "ZA",
      "region": "GT",
      "regionName": "Gauteng",
      "city": "Johannesburg",
      "zip": "",
      "lat": -26.2041,
      "lon": 28.0473,
      "timezone": "Africa/Johannesburg",
      "isp": "Google LLC",
      "org": "Google Cloud",
      "as": "AS396982 Google LLC"
   }
""".trimIndent()

fun getReservedRangeIpInformation() = """
    {
      "query": "192.0.2.1",
      "message": "reserved range",
      "status": "fail"
    }
""".trimIndent()

fun getEntryFileStringContents() =
    buildString {
        append("18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n")
        append("\n")
        append("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5\n")
        append("\n")
        append("1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3")
    }

fun getEntryFileInvalidContentFormat() =
    buildString {
        append("18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike")
        append("\n")
    }

fun getEntryFileContentsProcessed() = listOf(
    EntryFileContent(
        UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"),
        "1X1D14",
        "John Smith",
        "Likes Apricots",
        "Rides A Bike",
        6.2,
        12.1
    ),
    EntryFileContent(
        UUID.fromString("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7"),
        "2X2D24",
        "Mike Smith",
        "Likes Grape",
        "Drives an SUV",
        35.0,
        95.5
    ),
    EntryFileContent(
        UUID.fromString("1afb6f5d-a7c2-4311-a92d-974f3180ff5e"),
        "3X3D35",
        "Jenny Walters",
        "Likes Avocados",
        "Rides A Scooter",
        8.5,
        15.3
    )
)

fun getOutputFileContents() = """
    [
      {
        "name": "John Smith",
        "transport": "Rides A Bike",
        "topSpeed": 12.1
      },
      {
        "name": "Mike Smith",
        "transport": "Drives an SUV",
        "topSpeed": 95.5
      },
      {
        "name": "Jenny Walters",
        "transport": "Rides A Scooter",
        "topSpeed": 15.3
      }
    ]
""".trimIndent()

fun getWireMockStubForIPValidScenario() = wireMockServer.addStubMapping(
    WireMock.get(urlEqualTo("/json/149.250.252.66"))
        .willReturn(
            aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    """
                    {
                      "query": "149.250.252.66",
                      "status": "success",
                      "country": "England",
                      "countryCode": "GB",
                      "region": "BW",
                      "regionName": "Baden-Wurttemberg",
                      "city": "Böblingen",
                      "zip": "71034",
                      "lat": 48.6779,
                      "lon": 8.97297,
                      "timezone": "Europe/Berlin",
                      "isp": "EntServ Deutschland GmbH",
                      "org": "Triaton Frankfurt",
                      "as": "AS15854 EntServ Deutschland GmbH"
                    }
                """
                )
        ).build()
)