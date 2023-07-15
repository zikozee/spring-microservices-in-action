# GATEWAY-SERVER
- This is a _reverse proxy_ : i.e an intermediate server that sits between the client and the resource
- the clients request is captured and the calls the remote resource on the client's behalf 

## Mechanisms in routing
- Automated mapping of routes using service discovery
- Manual mapping of routes using service discovery

# mapping of routes via service discovery
- note if discovery service is available and the services are registered both automatic and manual routes will be shown 
- remove **discovery.locator**, then you will have the **_manual_** alone
- ```yaml
  spring:
    cloud:
      gateway:
        # Automatic routing
        discovery.locator:
          enabled: true
          lowerCaseServiceId: true
        # manual routing
        routes:
          - id: organization-service
          - uri: lb://organization-service

            predicates:
              - Path=/organization/**

            filters:
              #- StripPrefix=2   # to strip say the first api/v1  say we had api/v1/organization-service but the service has no api/v1
              # Rewrites the request path from /organization/** to /**
              - RewritePath=/organization/(?<path>.*), /$\{path}
  ```
- AUTOMATIC SERVICE CALL
  - then we call call organization service as this:
      - http://<GATEWAY_IP>:<GATEWAY_PORT>/<EUREKA_SERVICE_ID_FOR_ORGANIZATION>/<OTHER_URI>
      - http://localhost:8072/organization-service/v1/organization

- MANUAL SERVICE CALL
  - http://localhost:8072/organization/v1/organization



# List routes managed by Gateway server
- actuator/gateway/routes

## Cross-cutting concern
- Static routing
- Dynamic routing
- Authentication and authorization
- Metric collection and logging



# Isn't a service gateway a single point of failure and a potential bottleneck
- Load balancers are useful when placed in front of individual groups of services
- Keep any code you write for your service gateway stateless
- Keep the code you write in your service gateway light

# Capabilities
- Mapping the routes for all the services in your application to a single URL
- Building filters that can inspect and act on the requests and responses coming through the gateway
  - we can modify incom  ing and outgoing HTTP requests and responses
- Building predicates which are objects that allow us to check if the requests fulfill a set of given conditions
  - before executing or processing a request