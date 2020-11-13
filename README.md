# Bus Driver application
This is a simple spring boot application.

## Running

```
./mvnw spring-boot:run 
```

You can access the application on `http://localhost:8080`. Accessing it on any browser will redirect to `http://localhost:8080/swagger-ui/index.html`, the documentation of the API.

## Summary:

Example of the BusDriver object.

```json
{
  "age": 0,
  "birthdate": "2020-11-13",
  "id": 0,
  "name": "string"
}
```

## Available APIs

Assuming the base url: http://localhost:8080/api

GET /bus-drivers
```
curl -X GET "http://localhost:8080/api/bus-drivers" -H "accept: */*"
```

POST /bus-drivers
```
curl -X POST "http://localhost:8080/api/bus-drivers" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"age\": 0, \"birthdate\": \"2020-11-13\", \"id\": 0, \"name\": \"string\"}"
```

PUT /bus-drivers
```
curl -X PUT "http://localhost:8080/api/bus-drivers" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"age\": 0, \"birthdate\": \"2020-11-13\", \"id\": 0, \"name\": \"string\"}"
```

GET /bus-drivers/{id}
```
curl -X GET "http://localhost:8080/api/bus-drivers/1" -H "accept: */*"
```

DELETE /bus-drivers/{id}
```
curl -X DELETE "http://localhost:8080/api/bus-drivers/1" -H "accept: */*"
```