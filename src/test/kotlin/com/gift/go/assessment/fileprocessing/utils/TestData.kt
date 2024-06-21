package com.gift.go.assessment.fileprocessing.utils

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