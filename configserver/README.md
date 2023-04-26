# Config Server
- add @EnableConfigServer
- reading from local file system, we must set the profile to **native**
- on startup the profile used must be native

## application configuration naming convention
- if  <APPNAME>.properties is present without environment, it is loaded first
-  <APPNAME>-<ENVIRONMENT>.properties  or <APPNAME>-<ENVIRONMENT>.yml
  - e.g licensing-service-dev.properties, licensing-service-prod.yml

- springframework does the work of overriding properties in default profile with chosen profile
- spring cloud loads all i.e default then the chosen environment

## added the below to load boobstrap.yml properly
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

## chose to use layered form of Dockerfile
```xml
<layers>
   <enabled>true</enabled>
</layers>
```
- step 2 **mvn clean package**
- step 3 **java -Djarmode=layertools -jar target/config-server-0.0.1-SNAPSHOT.jar list**
- docker build -t <name:version> .

## precedence of spring.profiles.active for config server
- the last profile is the winner
- ```yaml
    spring:
      profiles:
        active:
        - native, git   ## git is the winner
```
