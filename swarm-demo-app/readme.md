## Simple rest endpoint exposed using WildflySwarm ##


Demo application exposing few rest endpoints, some of which are secured with JWT for demonstration purposes.
Exposed rest endpoints are:
 
### Building and running ###

Requirements:

1. Java 8
2. Maven (min 3.3.9)

Build and run:

To build and run do mvn clean install &&  java -jar restful-demo-application/target/restul-demo-application-1.0.1-SNAPSHOT-swarm.jar 
 
### Public ###
 
 1. Simple HTTP GET:
 HTTP GET: http://localhost:8080/api/v1.0.0/public/data
 
 Will return Content-Type: text/plain and body: Some public data
 2. Login HTTP GET:
 HTTP GET: http://localhost:8080/api/v1.0.0/login?username=dejan&password=kitic
 
 Will return Content-Type: text/plain and header Authorization: "Bearer JWT", where JWT is valid json web token.
 Body will be empty.
 
### Secured ###
 
 HTTP GET: http://localhost:8080/api/v1.0.0/secure/data
 
 Will return 405 if Authorization header does not contain valid JWT, or will return Content-Type: text/plain and body: Some super secret data
 
 
 
 