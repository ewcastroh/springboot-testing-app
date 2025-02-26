# Spring Boot Project

## Overview
This project is a Spring Boot application designed to provide scalable and maintainable backend services. It leverages the Spring ecosystem to ensure efficiency and reliability.

## Features
- RESTful API development
- Integration with external services
- Secure authentication and authorization
- Database interaction using Spring Data
- Configuration management

## Technologies Used
- Java (latest version)
- Spring Boot
- Spring Framework
- Spring Data JPA
- Spring Security
- GCP BigQuery SDK (if applicable)
- Maven/Gradle
- Docker (optional)

## Getting Started
### Prerequisites
Ensure you have the following installed:
- Java (latest LTS version)
- Maven or Gradle
- Docker (if needed)

### Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/spring-boot-project.git
   ```
2. Navigate to the project directory:
   ```sh
   cd spring-boot-project
   ```
3. Build the application:
   ```sh
   mvn clean install
   ```
   or
   ```sh
   gradle build
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```
   or
   ```sh
   java -jar target/spring-boot-project.jar
   ```

## API Endpoints
TBD (to be documented as the project progresses)

## Testing
Testing strategies using Spring and Spring Boot testing tools will be documented here.

## OpenAPI Specification
Open URL: 
```url
http://localhost:8080/v3/api-docs`
```

Customize the path in application.properties using the springdoc.api-docs property. For example, we can set the path to /api-docs:
```properties
springdoc.api-docs.path=/api-docs
```

Then, we’ll be able to access the docs at:
```url
http://localhost:8080/api-docs
```

By default, the OpenAPI definitions are in JSON format. For yaml format, we can obtain the definitions at:
```url
http://localhost:8080/api-docs.yaml
```

### Swagger UI
Open URL: 
```url
http://localhost:8080/swagger-ui.html
```

Support for swagger-ui Properties
```properties
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

More info: https://springdoc.org/ and https://www.baeldung.com/spring-rest-openapi-documentation


## Contributing
Feel free to fork the repository and create a pull request with improvements.

## License
This project is licensed under the MIT License.

