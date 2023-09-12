
## jwk.uri
- call http//<AUTH-SERVER>/.well-known/openid-configuration
- then check for jwks_uri ::: the endpoint is what will be used 


## purpose of jwtAuthenticationConverter::: CustomJwtAuthenticationTokenConverter
- this is used to move the authorities from within authentication.principal.claim
    - to authentication.authorities
