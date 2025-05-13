# OpenAPI Generator for the default library

## Overview
The intent of the project is to provide generated SDK artifacts from the FMSF v2 OpenAPI document, that can be used to integrate API consumer with this service.
The OpenAPI generator has many options and the SDKs generated here were selected for our immediate clients, but you may use the structure to generated your own SDK according to your needs.

### Generated Client SDKs:
- Java using Apache HTTP Client
- Java using Spring WebClient
- Java using Jersey 3 and Java HTTP client (TODO)
- Postman Collection
- TypeScript-Angular (TODO)
- TypeScript-Node (TODO)

### Generated Server Artifacts:
- Java using SpringBoot Server (Not Tested)

------
##### What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.
When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic.
Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more.

