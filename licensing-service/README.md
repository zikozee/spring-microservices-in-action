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