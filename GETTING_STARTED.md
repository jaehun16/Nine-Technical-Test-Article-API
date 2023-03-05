# Setup/Installation Instructions

## Overview
This API application uses Spring Boot with JPA/Hibernate. It configures H2 as database. 
This solution uses maven as build tools with JDK 19.

## Build Environment
1. Install [Homebrew](https://docs.brew.sh/Installation), a package manager on macOS.
1. The command `brew install maven` will install the latest Maven.
```
 brew install maven
 mvn -version
```
3. The command `brew install openjdk@19` will install the latest JDK 19.
```
brew install openjdk@19
```
4. The command `brew install jq` will install a lightweight and flexible command-line JSON processor.

## Build Application
1. The command `mvn clean package` will build the application including tests. 
```
mvn clean package
```

## Run Application
### Use Maven
> mvn spring-boot:run
### Use Java
> java -jar target/article-api-0.0.1-SNAPSHOT.jar 

## Check Application
### H2 database console
The configuration enables H2 console. 
Use the H2 console to check that the DB is up and running.
> http://localhost:8090/h2-console
* JDBC URL is jdbc:h2:file:~/testdb and there is no password

### API endpoints
> POST http://localhost:8090/articles

> GET http://localhost:8090/articles/{id}

> GET http://localhost:8090/tags/{tagName}/{date}
 
### Sample CURL commands
#### Post a new article
> curl -X POST http://localhost:8090/articles -H 'Content-Type: application/json' -d '{"title": "latest science shows that potato chips are better for you than sugar","date" : "2016-09-22","body" : "some text, potentially containing simple markup about how potato chips are great","tags" : ["health", "fitness", "science"]}'
#### Post a new article without date - Today
> curl -X POST http://localhost:8090/articles -H 'Content-Type: application/json' -d '{"title": "latest science shows that potato chips are better for you than sugar","body" : "some text, potentially containing simple markup about how potato chips are great","tags" : ["health", "fitness", "science"]}'
#### Get an article
> curl -X GET http://localhost:8090/articles/1 | jq
#### Get information related tag and date
> curl -X GET http://localhost:8090/tags/health/20160922 | jq