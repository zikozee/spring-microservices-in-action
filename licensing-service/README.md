commands

jar tf <JAR_FILE>   -- to show all object in jar
# CREATING DOCKER IMAGES

## 1.)  adding dockerfile-maven-plugin
- plugin
```xml
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.4.13</version>
                <configuration>
                    <repository>${docker.image.prefix}/${project.artifactId}</repository>
                    <tag>${project.version}</tag>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
                <executions>
                    <execution>
                        <id>default</id>
                        <phase>install</phase>
                        <goals>
                            <goal>build</goal>
                            <goal>push</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

```
- cmd to run :::: mvn package dockerfile:build

## 1.) Creating docker images with springboot
- criteria Spring Boot v2.3
- docker and docker compose installed 
- build packs
  - tools that provide application and framework dependencies, transforming ou r  source code into runnable application image
  - spring boot 2.3.0 introduces support for building docker images using cloud native buildpacks.
  - support as added to maven and gradle through the **spring-boot:build-image** goal
- customizing image
```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <configuration>
    <image>
      <name>${docker.image.prefix}/${project.artifactId}:latest</name>
    </image>
  </configuration>
</plugin>
```
- spring-boot:build-image


## 3.) Layered Jars
- step 1 add the following plugin
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
            <layers>
            <enabled>true</enabled>
        </layers>
    </configuration>
</plugin>
```

- step 2 **mvn clean package**
- step 3 **java -Djarmode=layertools -jar target/licensing-service-0.0.1-SNAPSHOT.jar list**
  - this will show a list as below
    - dependencies
      spring-boot-loader
      snapshot-dependencies
      application

- step 4 - create docker file for layered jars
```yaml
FROM openjdk:11-slim as build
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-slim
WORKDIR application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

- docker build -t <name:version> .



- configuring the license service to use Spring Cloud Config
  - Usually, the information you store in the **application.yml** is configuration data that you might  want to have available to a service even if the Spring Cloud Config service is unavailable
```yaml
spring:
  application:
    name: licensing-service  # this name is used bySpring Cloud config Client to know which service is being looked up in the config server
  
  profiles:
    active: dev       # specifies the default profile to run
    
  cloud:
    config:
      uri: http://localhost:8071  # specifies the location of the Spring Cloud Config server


  ## docker dev in config yaml ##
example:
  property: I AM DEV
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/ostock_dev
    username: postgres
    password: postgres

```

## Configuration used and called from config server
- http://localhost:8080/actuator/env
  - this shows the profile, config-server file used with its properties
## Refreshing custom properties
- this is achieved using @RefreshScope  
  - note only custom properties are refreshed, database config are not refreshed
  - there's another way we can achieve same process  using Spring Cloud Bus 
    - this requires RabbitMQ
  - but we can always restart our application with new config as docker makes this simple 
    - (make your app stateless)  12 factor app



## Spring/Netflix Client Libraries for service lookup
- Spring Discovery Client
- Spring Discovery Client-enabled  Load Balancer-aware REST template
- Netflix Feign client

  ###  Spring Discovery Client
  - Note this comes with several issues
    - you aren't taking advantage of the Spring cloud client-side Load Balancer **(instances.get(0))**
    - you are doing too much work (writing extra code)
    - you need to instantiate RestTemplate directly else using the autowired way will change how URI are constructed
      - and this is antithetical in spring
  - this offers the lowest level of access to the Load Balancer and the services registered within it
  - we need use the annotation:  **@EnableDiscoveryClient**
  - we retrieve all instance of the service we are looking for using the service key (i.e the spring.application.name)

  ###  Spring Discovery Client-enabled  Load Balancer-aware REST template
  - usage  :: define a **RestTemplate Bean** with a Spring Cloud **@LoadBalanced** annotation
  - the major difference in the way the RestTemplate is constructed  with the Spring Discovery client is that 
    - we use the Spring.application.name (applicationId) as server name
    - i.e http://{applicationId}/v1/organization/organizationId
  - this applicationId is used as key to query the load balancer in round-robin manner to fetch the service instance

  ### Netflix Feign client
  - usage :: we need use annotation **@EnableFeignClients**
  - we create an interface with  annotation **@FeignClient("application-id")**
  - we use signatures just exactly how we would create Controller class
  
## Bulk Head Patten - allow X number of CONCURRENT calls at a time
- we definitely expose several endpoints to our customers
- however, we notice a call to a particular endpoint keeps timing out or starving resources
- we can limit the number of calls to this particular endpoint using bulkhead
- types of bulkhead -> semaphore (setting max concurrent calls) or Thread pool bulkhead (using a thread pool)

## Retry
- use max-attempts, wait-duration in conjunction with exponontial-backoff
- result predicate help retry on certain result
- retry-exception-predicate should be should if we expect a certain exception message or action, rather than the general exception (i.e check particular message rather than the entire Exception see CustomRetryException usage)  ELSE just use the retry-exception
- for retry-Exception, ignore-Exception, result-predicate and retry-exception-predicate use the full classpath

## Rate Limiter -> allow X  number of calls every Y second 
- in cloud up-to-date - architectures, its a good option to include auto-scaling

## jwk.uri
- call http//<AUTH-SERVER>/.well-known/openid-configuration
- then check for jwks_uri ::: the endpoint is what will be used 
- this uri is used alternatively to setting up the private key on the auth server and the public key on the resource server
  - which can be tedious in rotation
  - however, the uri case, only the auth-server keys need to be rotated

## purpose of jwtAuthenticationConverter::: CustomJwtAuthenticationTokenConverter
- this is used to move the authorities from within authentication.principal.claim
  - to authentication.authorities

## if you need to add more custom details to the token extend JwtAuthenticationToken directly
- CustomJwtAuthenticationToken extends JwtAuthenticationToken
