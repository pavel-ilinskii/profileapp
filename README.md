# Profile Test Application

### Prerequisites
Make sure you have installed all of the following prerequisites on your machine:
* Java 11
* PostgreSQL 12

Create database (example: profileapp)

### Configuration
Open dist/config.yml and set following properties:
* spring.datasource.url : jdbc connection string to PostgreSQL database
* spring.datasource.username : database username
* spring.datasource.password : database password
* api-key : auth token

### Run
    cd dist
    java -jar profileapp-1.0.0.jar --spring.config.name=config

### Security
Add X-API-Key header to request with api-key value

Example

    curl -X GET "http://localhost:8010/error/last" -H "accept: */*" -H "X-API-Key: secret"
    
### Swagger
Open in browser http://localhost:8010/swagger-ui.html
Swagger is configured for X-API-Key header

### Exit
Open in browser http://localhost:8010/exit
 

