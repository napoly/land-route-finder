# Country land route BFS border search reactive Spring Boot microservice

### Reference Documentation

[Spring Boot](https://spring.io/projects/spring-boot) microservice build on top of [Project Reactor](https://projectreactor.io/). The code is written in [Kotlin](https://kotlinlang.org/) for [JDK 17](https://openjdk.org/projects/jdk/17/).  

A list of countries is provided in resources in [JSON](https://www.json.org/json-en.html) format. Uses [JsonSchema2Pojo](https://www.jsonschema2pojo.org/) and [Maven](https://maven.apache.org/) for its model generation.

### Guides

One can run this project by importing it into [IntelliJ](https://www.jetbrains.com/idea/) community edition. Installing [OpenJDK](https://openjdk.org/) through [SDK man](https://sdkman.io/) and re-running maven lifecycle. Idea run configurations can be found in `.run` folder. The project uses [Swagger](https://swagger.io/) deployed on [Netty](https://netty.io/) reachable at port 8080. Application exposes endpoint on `/routing/{origin}/{destination}` where origin and destination countries are represented in [ISO_3166-1_alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) format.