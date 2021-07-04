# Netflix
* How to use the service
   * Run below command 
     * java - jar -Dspiring.profiles.active=CompatibleStreamService CompatibleStreamService-0.0.1-SNAPSHOT.jar
     * Service will start on 8080 port on default tomcat embedded server
     * POST URL - http://localhost:8080/compatibleStreamingData/
``` Json
Request Json- {
    "movieId": 2,
    "memberId": "4",
    "maxBitrate": 500,
    "encodes" : [
        "SOL",
        "PROXIMA",
        "WOLF"
        
    ]
    
}

Response Json-
{
  "error": String,
  "success": boolean,
  "streams":[
   {
      "streamId":long,
      "streamType":String,
      "streamEncode":String,
      "streamBitrate": int,
      "primaryUrl":String,
      "secondaryUrl": String
   }
 ]
}
```
## Assumptions Made

* Size of the payload retrieved from stream service is within the memory limits of the system/application.
* Responses from the given services are constant and not changing over time. Also as per Http specifications, Get API responses are idempotent, so assuming the same responses would be returned from stream/member/Cdn services for a particular input parameter.
* Number of concurrent requests this application can handle is 200 as the embedded default tomcat server is being used.
* As per HTTP specifications, GET API containing the request body might fail or behave in an unexpected manner. Thus, using a POST API for the CompatibleStreamingService. 
* Even though it's a POST API, this service is not creating any resource or storing any information in databases yet.  


## Proposed design and solution implemented so far

* Checks if the member is valid or not. If a member is not valid, it will not return any streams, instead returns an error message which says member is not valid.
* Memberservice and streamservice are executed in parallel as the two services are independent of each other and are mutually exclusive.
* Used reactive programming-  observables and observer pattern in order to avoid I/O blocking calls such as network calls to member, stream and CDN services. 
* Created two Observables one for member service and the other for stream service using just method. 
* Created a Zip Observable so that observable waits for the emission of the responses of both member and stream service and checks if the memberId is valid or not. If the member is not valid, it returns an “invalid member” , otherwise calls CDNservice to get the cdn locations for each streamId.

## Improvements to be made/TODO's
1. Used a simple concurrentHashMap as in-memory cache. Size of the cache is not taken into consideration; Did not implement cache eviction strategy
2. Unit tests
3. Request Validation - Did not implement specific validations on the enums allowed yet.
4. Security. If the service is going to be externalized, I would implement authentication and authorization/scopes for a specific user.
5. CDNService, StreamService and MemberService are running on tomcat servers. This service is also built to use the default tomcat embedded server. I would use netty over tomcat server as tomcat has a thread per request unlike netty event loop framework limiting on the number of concurrent connections the service can have.
6. Need to implement/think on the fallback logic when services fail/do not respond on time. Hystrix can be used for the fall back.
7. Unit Tests are not written yet.
8. Yet to cover exhaustive HTTP response codes.


## Timeouts / retry behavior
* Member service observable  has a Timeout of 50ms after which it will retry 2 times without any delay
* Stream service observable  has a Timeout of 50ms after which it will retry 2 times without any delay
* CDN service observable  has a Timeout of 20ms after which it will retry 2 times without any delay
* Fallback logic if the services are down is not yet implemented.

## Technology Stack
* Java 1.8 
* Rxjava. 
* SpringBoot - 2.5.2
* Maven 
* Embedded Tomcat server


## Dependencies Used
* Springboot starter web and other spring boot related dependencies
* Rxjava for reactive/asynchronous programming
* slf4j for logging purpose


## Trade Offs/Thoughts on different ways
* Tomcat vs netty server. This application is currently running on tomcat with thread per request model. Can be improved to use netty server to make use of event loop and have more connections with less threads. 
* The above can be achieved using spring webflux dependency 
* WebTarget Vs WebClient. This application uses webtarget to connect to services. This can be enhanced to use netty clients. 




## Non functional requirements
* Performance and Latency
  * When did a network call. It took ~2.3secs
  * When Loaded from Cache. it took ~70msec

* Scalability
	* Currently this service is running on a single server, and can take upto 200 concurrent requests. However, in order to handle billion’s of requests, we need to increase servers.
   ```
   Lets say each request takes 2sec which is the latency
   (No of concurrent connections per server * No of servers) = Total no of requests per sec * Latency
   No of servers = (Total no of requests per sec * Latency)/No of concurrent connections per server	
* Load testing
  * Service should continue to have the same latency even if the load on the application increases indefinitely.
  * Used a postman collection runner to run multiple requests at the same time without any delay. 
## Edge Cases
1. Member Service timeouts/ returns error 
2. StreamService timeouts/returns error
3. Cdn service timeouts/return errors
4. Both the above services timeout/return errors
 







