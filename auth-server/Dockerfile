## BASIC DOCKER FILE START

##Start with a base image containing Java runtime
#FROM openjdk:11-slim
#
## Add Maintainer Info
#LABEL maintainer="Ezekiel Eromosei <ezekiel.eromosei@gmail.com>"
#
## The application's jar file  - defines the JAR_FILE variable set by dockerfile-maven-plugin
#ARG JAR_FILE
#
## Add the application's jar to the container
#COPY ${JAR_FILE} app.jar
#
##execute the application
#ENTRYPOINT ["java","-jar","/app.jar"]

## BASIC DOCKER FILE END



## DOCKER MULTI_STAGE BUILD

##stage 1
##Start with a base image containing Java runtime
#FROM openjdk:11-slim as build
#
## Add Maintainer Info
#LABEL maintainer="Ezekiel Eromosei <ezekiel.eromosei@gmail.com>"
#
## The application's jar file - defines the JAR_FILE variable set by dockerfile-maven-plugin
#ARG JAR_FILE
#
## Add the application's jar to the container
#COPY ${JAR_FILE} app.jar
#
##unpackage jar file
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)
#
##stage 2
##Same Java runtime
#FROM openjdk:11-slim
#
##Add volume pointing to /tmp
#VOLUME /tmp
#
##Copy unpackaged application to new container - copies the different layers from the first image named build
#ARG DEPENDENCY=/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#
##execute the application
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.license.LicenseServiceApplication"]



## DOCKR FILE FOR LAYERED JARS
#FROM openjdk:21-slim as build
FROM amazoncorretto:21 as build
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:21
WORKDIR application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]