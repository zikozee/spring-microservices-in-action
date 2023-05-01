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
        - native, git, vault   ## the last vault is the winner
  ```


## Integrating Vault with Spring Cloud Config Service
- pull vault image
- docker run -d -p 8200:8200 --name vault -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' vault
- access ui http://localhost:8200/ui/vault/auth
  - token,  value= myroot
  - Enable new Engine
  - Select generic KV
  - Fill out the KV Secrets data
  - ensure you remember the path name e.g licensing-service
  - Then create secret after enabling engine and define the required property to encrypt
  - connect via: 
    - **curl -XGET http://localhost:8071/licensing-service/default -H "X-Config-Token: myroot**
    - OR define authentication in property 


## encrypting properties
- we can set as property encrypt.key in bootstrap.yml  (12 set of random characters)
- we can also set in environment variable: **ENCRYPT_KEY=IMSYMMETRIC**
- by adding this property config server makes available **/encrypt** and **/decrypt** endpoints