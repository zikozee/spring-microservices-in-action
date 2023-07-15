# GATEWAY-SERVER


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
  - we can modify incoming and outgoing HTTP requests and responses
- Building predicates which are objects that allow us to check if the requests fulfill a set of given conditions
  - before executing or processing a request