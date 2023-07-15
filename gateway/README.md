# GATEWAY-SERVER
- This is a _reverse proxy_ : i.e an intermediate server that sits between the client and the resource
- the clients request is captured and the calls the remote resource on the client's behalf 

## Mechanisms in routing
- Automated mapping of routes using service discovery
- Manual mapping of routes using service discovery

# Automated mapping of routes via service discovery
- ```yaml
  spring:
    cloud:
      gateway:
        discovery.locator:
          enabled: true
          lowerCaseServiceId: true
  ```
- then we call call organization service as this:
  - http://<GATEWAY_IP>:<GATEWAY_PORT>/<EUREKA_SERVICE_ID_FOR_ORGANIZATION>/<OTHER_URI>
  - http://localhost:8072/organization-service/v1/organization

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