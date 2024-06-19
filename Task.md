Springboot - Technical Assessment

The activity, stack and requirements are listed below.

Mandatory Task

Stack:
-	Java 17 Or Kotlin 1.8
-	Spring-boot 2.7+ or 3+


Scenario:
-	You are developing a system that takes a file (defined below) and processing it to create an 'Outcome file'.
-	You must use REST to trigger the application, this can be done by sending the initial file using HTTP POST.
-	You must parse the file and pull the details needed to create the 'Outcome File'.
     o	Please note, validation of the file is important for this activity.
-	Once the end file had been created, it needs to be passed back to the user.
-	There should be a feature flag added to skip the validation.


Notes:
-	In this activity we will be looking into your ways of thinking and choices of actions therefore it is NOT important to complete all the points listed in the scenario section.
-	We will be looking at your familiarity with spring boot, java/kotlin and overall theory.
     o	Specifically, the SOLID principles.

Initial file:
Name: EntryFile.txt
Type: TXT

Content:
18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1

3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5

1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3


Content Structure:
UUID, ID, Name, Likes, Transport, Avg Speed, Top Speed

OutcomeFile:
Type: JSON
Name: OutcomeFile.json

Content Structure:
Name, Transport, Top Speed


Additional Task/Requirements for Senior Developer

Before processing the file, you are to validate that the request comes from a valid/whitelisted IP address.

To validate that, you are to make a REST API call to

http://ip-api.com/json/{query}

Documentation of the API can be found in https://ip-api.com/docs/api:json

Validation rules:
1.	You must block IPs from the following countries
      a.	China
      b.	Spain
      c.	USA
2.	You must block IPs that are from the following ISPs/Data Centers
      a.	AWS
      b.	GCP
      c.	Azure
3.	Request that are blocked should be returned with HTTP code 403 with an error message that specifies the reason for the blocking.

For every request for file processing, you should be logging the following information in a relation database (MySQL, PostgresSQL, H2 Database etc)
1.	Request Id – This can be simply a generated UUID.
2.	Request Uri
3.	Request Timestamp – This should be the timestamp when the request reached the application
4.	HTTP Response code – 200, 403 , 500 etc
5.	Requset IP Address
6.	Request Country Code
7.	Request IP Provider – The provider (ISP) of the IP address
8.	Time Lapsed – The time taken (in milli seconds) to complete the request


Other than the necessary unit tests, you should be writing a REST API test to test your task. You MUST make use of Wiremock to mock the response from http://ip-api.com/json/{query}



Submission
Once you have completed the below activity, could you please upload the code to GitHub and send across a link for us to access.
