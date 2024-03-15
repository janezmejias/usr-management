# usr-management-ms

The microservice facilitates user registration through a RESTful API, processing JSON-formatted data including user details and phone information. It responds with a JSON object containing user identifiers and status upon successful registration, while also handling errors, such as duplicate emails, with descriptive messages. Designed to strictly adhere to RESTful principles and JSON communication, the service ensures robust data validation and clear, informative responses for an efficient and secure user registration experience.

### Starting the Application

Run the `KApplication` class


![diagramclass.jpeg](img%2Fdiagramclass.jpeg)
high-level architecture for a Spring Boot application concerning user management, particularly focused on security and user registration functionality

### Sequence Diagram
![seqdiagram.jpeg](img%2Fseqdiagram.jpeg)

### Component Diagram
![componentdiagram.jpeg](img%2Fcomponentdiagram.jpeg)

## OpenAPI definition
* http://localhost:8080/swagger-ui/index.html
![open-api.png](img%2Fopen-api.png)

## H2 Console

In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

http://localhost:8080/api/user/h2-console

Enter the information for the url, username, and password in the application.yml:

```yml
url: jdbc:h2:mem:testdb
username: sa 
password: password
```

You should be able to see a db console now that has the Sample Repository in it.

Type:

```sql
SELECT * FROM PHONE;
SELECT * FROM USERS 
````

Click `Run`, you should see two rows, for ids `1` and `2`

### Hexagonal Architecture
```batch
.
└── usermanagement
    └── core
        ├── application
        │   └── config
        │       ├── CustomConfiguration.java
        │       ├── exceptions
        │       │   └── BaseException.java
        │       ├── JwtAuthenticationFilter.java
        │       └── SecurityConfig.java
        ├── domain
        │   ├── dto
        │   │   ├── PhoneRequest.java
        │   │   ├── SignUpResponse.java
        │   │   └── UserRequest.java
        │   ├── mapper
        │   │   └── UserMapper.java
        │   └── service
        │       └── UserService.java
        ├── infrastructure
        │   ├── controller
        │   │   ├── advice
        │   │   │   ├── BaseExceptionHandler.java
        │   │   │   └── ResponseError.java
        │   │   └── UserController.java
        │   └── repository
        │       ├── entities
        │       │   ├── Phone.java
        │       │   └── User.java
        │       ├── PhoneRepository.java
        │       └── UserRepository.java
        └── KApplication.java

14 directories, 17 files

```

#### cURL Example
```bash
curl --location 'http://localhost:8080/api/user/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Juan Anez",
    "email": "janezmejias.09@gmail.com",
    "password": "Devbloc.09",
    "phones": [
        {
            "number": "3005650780",
            "cityCode": "1",
            "countryCode": "57"
        }
    ]
}'
```


## Unit Test
### Run All Test
```batch
gradle test
```

### Coverage
![all-converag.png](img%2Fall-converag.png)
### SonarLint
![sonar-lint.png](img%2Fsonar-lint.png)