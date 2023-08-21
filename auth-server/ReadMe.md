# GRANT TYPES
## a) Authorization Code
# http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=https://springone.io/authorized
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
  -  call method http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=https://springone.io/authorized&code_challenge=<CODE_CHALLENGE>&code_challenge_method=S256
- send post request to the below url
  - http://localhost:8080/oauth2/token?grant_type=authorization_code&code=<CODE>&redirect_uri=https://springone.io/authorized&client_id=client&code_verifier
  - Params: client id, grant_type, redirect uri, code gotten, code verifier 
  - with client id and secret as basic auth

## get public key for resource server to use
- http://localhost:8080/oauth2/jwks