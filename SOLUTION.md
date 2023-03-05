# Solution

## Overview
This API application has three endpoints using Spring Boot with JPA/Hibernate.
It uses Spring Initializr to bootstrap a Spring Boot application. 
It uses only the needed configurations and dependencies to integrate Hibernate, 
adding the Web, JPA, and H2 dependencies.

## Language and Libraries
This solution uses Spring Boot to create Java applications. 
Spring Boot applications need very little Spring configuration 
and provides a radically faster and widely accessible getting-started experience 
for all Spring development.
### System Requirements

| Build Tools | Version |
| --- | --- |
| Java SDK | v17 or higher |
| Maven | 3.5+ |

### Language
Spring Boot(v3.0.2) requires Java 17 or higher and supports maven as a build tool. 
This solution uses the latest version of java SDK, Java 19.

### Libraries
#### spring-boot-starter-parent (v3.0.2)
It is a special starter project that provides default configurations for an application 
and a complete dependency tree to quickly build a Spring Boot project. 
It also provides default configurations for Maven plugins.
> Spring Boot 3.0.2 requires Java 17 and is compatible up to and including Java 19


#### spring-boot-starter-web
It is used for building web layer, including REST APIs, applications using Spring MVC. Uses Tomcat as the default embedded container.
#### spring-boot-starter-data-jpa
It includes spring data, hibernate, HikariCP, JPA API, JPA Implementation (default is hibernate), JDBC and other required libraries.
#### spring-boot-starter-test
It is used to test Spring Boot applications with libraries including JUnit, Hamcrest and Mockito.
#### h2
Though adding any database easily using datasource properties in application.properties file, it uses h2 database in reduce unnecessacery complexity.


## Structure
This solution is a Spring Boot Application built by Maven.
Maven uses pom.xml file as the recipe that is used to build your project. 
This project uses a common directory layout of a Maven project.
### Project Directory
| Directory | Description |
| --- | --- |
| `src/main/java` | Application/Library sources |
| `src/main/resources` | Application/Library resources |
| `src/test/java` | Test sources |
| `src/test/resources` | Test resources |
| `README.txt`  | Project's readme  |

### Code Package
| Package | Description |
| --- | --- |
| `nine.jaehun` | Root of Application |
| `nine.jaehun.article` | Root of Domain including a Controller |
| `nine.jaehun.article.entity` | Entity classes |
| `nine.jaehun.article.repository` | JPA Repository Interfaces |
| `nine.jaehun.article.entity` | Service Interfaces and Implements |

## Error handling
For simplicity, this application uses the default RestExceptionHandler to handle errors. 
Each endpoint throws a ResponseStatusException when it catches any error. 
There are two types, HttpStatus.NOT_FOUND and HttpStatus.BAD_REQUEST.

## Approached testing
This solution uses JUnit to run unit tests of three cases in the application. 
The first case is for Error handling (ApplicationErrorHandleTests). 
The second one is to test a service(ArticleServiceUnitTest).
The last is to test each endpoint(ApplicationTests). 
Every build time, the three test cases will be triggered to check any changed code to meet the requirements.

## List of Assumptions
### POST API `/articles` 
* There are two case related the date field. 
One is with a particular date and the other one is without date. 
The case without data means the article is today.
* Larger IDs are more recent articles

### GET API `/tags/{tagName}/{date}`
1. This api will query all articles on the given date.
2. It'll count the number of the all tags of all the articles and store it at count field.
3. It'll list up tags that are on the articles which has the given tag. 
The related_tags field contains a list of tags excluding the given tag.
4. It'll list the most recent 10 articles among the viewed articles and select articles that has the given tag. 
The articles field contains a list of ids of selected articles.