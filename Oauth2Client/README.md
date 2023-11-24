## OAUTH2CLIENT

- org.springframework.security.config.oauth2.client.CommonOAuth2Provider
    - with how ProjectConfig.clientRegistrationRepository was built


- this guy just calls the authorization server with the client configuration the access resources on behalf of the client

- if there are multiple auth server, you just need to create multiple client registration with the respective config
- load from DB, give the user an id or so then when that user pass that id, call its details from the db
  

## TEST
- curl -XGET http://localhost:7071/token

## SAMPLE RESPONSE

$ curl -XGET http://localhost:7071/token
eyJraWQiOiI0MjFhODA4NC1iNDMwLTRhOTctOWMyYy0zYmY2MTlhODNjMDEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjbGllbnQiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE3MDA4MTU5NzIsInRlc3QiOiJ0ZXN0Iiwic2NvcGUiOlsib3BlbmlkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA3Mi9hdXRoIiwiZXhwIjoxNzAwOTAyMzcyLCJpYXQiOjE3MDA4MTU5NzIsImF1dGhvcml0aWVzIjpbXX0.j9J51EX1oMOa_xmvtAlw_yBfXbDlQUUK0ay_MUk7rha-62Jtzm5hoD_BpU1HN-grKVkXjW3E2Ilu6D_tEdbvGUr9zLA42KrjE51sUXlD6WQ2zhn8UR64lPP36gUQU6OEeIoAGX0gi1-Fc-FpkAD-AFrLb02axmpnIVO_UbEPteOtWQsy5l0Dhf8Mlq5aftEZmJbEj0DGYuAwNW5oAZpeFJVr_Bmczc1fQgXk2tFeTj0bVEp-ZocjGz2tSGfM5TSAuNGUe0rxIX5LNvOR7hfAyVKRPg0jNr8GlKM09YjjTkVcCEMn5dLVcXSsmqtAmQ-xkJeugMMub0Gsq38TtcIoMA
zikoz@EZEKIEL MINGW64 ~/Desktop/JAVA/MAVEN/2023_PROJECTS/MARCH/spring-microservices-in-action (main)

$ curl -XGET http://localhost:7071/token
P-04PAfe-vGODcV4QnjL37BhSQ3SJlKDvUBi2YHxfoiwcIJc4VZiqGwNRXKZC8ae6tYk3Z2kEKar-9DGFTLlHabsubXMcdIujG0YUeeUnq3ss0roeaPucD8EoyB6FGQj
