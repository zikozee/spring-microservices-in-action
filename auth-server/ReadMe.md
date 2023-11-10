# GRANT TYPES

## NOTE:: if yu are connected to wlan and calling auth-server via gateway, make the call via the wlan IP  http://<WLAN_IP_AS_HOST>:<PORT> ...
## a) Authorization Code
# http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=https://springone.io/authorized
# http://<WLAN_IP>:<GATEWAY_PORT OR AUTH_PORT>/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=https://springone.io/authorized
- ensure the client id for the client is correct and the scopes allowed (space separated), also the redirect uri
- upon return, get the code
- send post request to the below url
  - http://localhost:8080/oauth2/token?grant_type=authorization_code&scope=openid&code=<CODE>&redirect_uri=https://springone.io/authorized&client_id=client
  - Params: grant_type, scope, code goten, redirect uri, client id
  - with client id and secret as basic auth

## b) Authorization code pkce
 // see test intro to j5
  -  generate codeVerifier  _this was hashed to become the challenge_
  -  generate code challenge
  - ensure you update the client_id
  -  call method http://localhost:8080/oauth2/authorize?response_type=code&client_id=clienty&scope=openid&redirect_uri=https://springone.io/authorized&code_challenge=<CODE_CHALLENGE>&code_challenge_method=S256
- send post request to the below url
  - http://localhost:8080/oauth2/token?grant_type=authorization_code&code=<CODE>&redirect_uri=https://springone.io/authorized&client_id=client&code_verifier
  - Params: client id, grant_type, redirect uri, code gotten, code verifier 
  - with client id and secret as basic auth

## get public key for resource server to use
- http://localhost:8080/oauth2/jwks

## Registered Client Repository using Jpa and JdBcTemplate
- Jpa: https://gist.github.com/sjohnr/0c6065d90d11a0f24bd3f15cbbaa8527
- JdbcTemplate: https://huongdanjava.com/store-registeredclient-to-database-in-spring-authorization-server.html

## Client credentials by Orchestrators
- say we need kubernetes to do a liveness and readiness probe on our service, then it must connect with client credentials


## user a OAuth2TokenCustomer bean  to add more details to your token
- here you can pull database details you wanna show
  - optionally you could always call the db from the resource to get what you need
- note however, the larger the claims the slower it is encrypted and slower it takes to be transmitted over http

## Multiple, grantypes, AuthMethod and redirectUris
- you could have several client that require different redirect_uri based on may web, mobile
- hence we could configure several URI for that client but on request, will specify in the url call as well as POST call


## CustomRedirectUriValidator
- added this in order to be able to remove restriction and validate in this case authorizationEndpoint ::: Testing Purposes alone
- you can basically customize anything within the Configurer i.e http.getConfigurer

## No Opaque Tokens
- e.g jwt  ->> No Opaque (SELF-CONTAINED) contains information inside it
- opaque (REFERENCE) contains no details however the details can be gotten by 
  - POST calling the _**introspection_endpoint**_ found in the _**http://localhost:8080/.well-known/openid-configuration**_
  - with params token : <opaque token>  with basic auth of [client id and client secret]

## disabling access token
- minimize time to live
- use the revocation endpoint found in the _**http://localhost:8080/.well-known/openid-configuration**_
- with params token : <access token>  and basic auth client id and client secret

## Difference between Scope And Authorities
- Scope are for the client, Authorities are for the user
- if you use openid protocol, you must use the predefined or customized scopes
- if you use Oauth2, you can use any kind of scope in this case our authorities, inclusive of the openid scopes

## purpose of jwtAuthenticationConverter::: CustomJwtAuthenticationTokenConverter
- this is used to move the authorities from within authentication.principal.claim
  - to authentication.authorities

## if you need to add more custom details to the token extend JwtAuthenticationToken directly
- CustomJwtAuthenticationToken extends JwtAuthenticationToken

## switching userdetails and clients to database
- added dependencies for jpa and mssql
- added entites and repositories
- design your table relation where a table hold all the supported auth_method, grantTypes, scopes
  - user 1 - * redirect uris
  - user * - * authorities
  - client 1 - * redirect uris
  - client * - * auth_method
  - client * - * scope
  - client * - * grant type
  - client 1 - 1 token settings

## Generating a private key
- keytool -genkeypair -alias tech -keyalg RSA -keypass 2023techAdventurers -keystore idea.jks -storepass 2023techAdventurers

## Obtaining the public key:: not needed (rotating only on private server)
- keytool -list -rfc --keystore idea.jks | openssl x509 -inform pem -pubkey