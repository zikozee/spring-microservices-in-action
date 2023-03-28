# Critical roles to building Microservices
- The architect --> sees the big picture, decomposes application into individual microservices and how they interact
- The software developer --> writes the code and understand how the language and framework will be used to deliver the service
- The Devops Engineer --> Determines how the services are deployed and managed throughout production and non-production environment
    - consistency and repeatability in every environment (dev, uat, staging, prod)


# The Architect
- Decomposing the business problem
- Establishing service granularity
- Defining the service interfaces(where services meet or interact)

## Decomposing the business problem
- Describe the business problem and the nouns you use to describe it
- Pay attention to the verbs --> gets, looks, look-up, updates, posts
- Look for data cohesion --> look for data that are highly related to each other
  - if you are reading or updating data that is radically different from what was discussed, you potentially have 
  - another service candidate. (Microservices must completely own their data)

## Establishing service granularity
- it is better to start broad with our microservice and refactor to smaller pieces
- Focus first on how services interact with one another
  - This helps establish the coarse grained interfaces of your problem domain
  - it is easier to refactor from coarse-grained than from being too fine-grained.

- Service responsibilities change over time as our understanding of the problem domain grows
  - what starts as a single microservice might grow into multiple service, with the original microservice acting
    - as an orchestration layer for these new services and encapsulating their functionality from other parts of the
    - application.

### Too Coarse-grained microservice
- A service with too many responsibilities 
  - general flow of business logic in the service is too complicated

- A service that manages data cross a large number of tables
  - rule of thumb -> 3 to 5 tables max. anymore is likely to have too much responsibility
  
- A service with too many test cases
  - service with hundreds of unit and integration tests, you might need refactor

### Too Fine-Grained microservice
- The microservices in one part of the problem domain breed like rabbits
  - if everything becomes microservice, composing business logic out of these services becomes complex and difficult
  - that's because the number of microservice needed to get a simple work done grows tremendously
  - common smell to know: if each microservice interacts with a single database table

- Your microservices are heavily dependent on each other
  - to complete a single user request, the are multiple services calling each other back and forth

- Your microservices become a collection of simple CRUD
  - microservices are an expression of business logic and not an abstraction layer over data sources
  - if your microservices do nothing but CRUD-related logic, they are probably too fine-grained.

## ALWAYS KNOW YOU WON'T GET IT RIGHT THE FIRST TIME (DEVELOP WITH AN EVOLUTIONARY THOUGHT)
- however, ensure you start from coarse grained than fine-grained
- don't be dogmatic with your design. Be willing to change and adapt
  - be pragmatic (dealing with things sensibly and realistically based on practical rather than theory)
  - Your design won't always be perfect but evolve and make changes where necessary

## Define the Service Interfaces
- make your service interfaces easy to understand and consumable
- defining how the microservices talk to each other
- these should be intuitive and developers should get a rhythm of how all the services work in the application by
  - fully understanding one or two of the services of the application
- Guidelines: 
  - Embrace the REST philosophy > GET, PUT, POST, DELETE
  - Use URIs to communicate intent -> **mobilemoney-customer-validation**, **mobilemoney-payment-notification**
  - Use JSON for request and response
  - Use HTTP status code to communicate results

# WHEN NOT TO USE MICROSERVICES
- Complexity when building distributed systems
- Virtual Server or container sprawl
- Application Type
- Data transaction and consistency

## Complexity when building distributed systems
- Microservices introduces a level of complexity
- Don't use microservice unless the organisations is willing to invest in the automation and operational work
  - i.e monitoring, scaling and so on that a highly distributed application needs to be successful

## Virtual Server or container sprawl
- ideally, one microservice to a container 
- production could run from 50 - 100s or containers
- though cloud makes these quite cheap bu the complexity of managing and monitoring these service is tremendous 

## Application Type
- if you a building a small departmental-level application rather than **"enterprise level"** with a smalluser base
  - go for a monolith

## Data transaction and consistency
- first understand the data usage patterns of your services and service consumers
- microservice works well by abstracting always a small number of tables and works well as a mechanism for performing
  - "operational" tasks like creating, adding, and performing simple (non-complex) queries against the data store
- if your application needs do a complex data aggregation or transformation across multiple datasource, the 
  - distributed nature of microservices wll make this work difficult, take on too uch responsibility
  - and introduce performance problems


# The software developer
- use RestController
- use Json for communication -extremely light
- use clearly named URLs
- use versioning early on

# DevOps 