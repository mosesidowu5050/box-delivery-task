# BoxDelivery

A Spring Boot project for managing boxes and items.  
This application demonstrates RESTful endpoints for creating, updating, and loading items into boxes.

## Prerequisites

- Java 17 or newer
- Maven Wrapper (`./mvnw` and `mvnw.cmd` are included)

## Build

To build the project from the command line:

```bash
./mvnw clean install
```

Or on Windows:

```bat
mvnw.cmd clean install
```

## Run

To run the application from the command line:

```bash
./mvnw spring-boot:run
```

Or by running the main class (`BoxDeliveryApplication`) from your IDE (IntelliJ IDEA is recommended).

The application will start on [http://localhost:8080](http://localhost:8080)

## Test

To run all tests:

```bash
./mvnw test
```

## Endpoints

- `POST /api/boxes/create-box` – Create a new box
- `POST /api/boxes/{id}/load-items` – Load items into a box
- `GET /api/boxes/{id}/get-items` – Get loaded items for a box
- `GET /api/boxes/get-available-items` – List boxes available for loading
- `GET /api/boxes/{id}/battery-level` – Check battery level

## API Usage

You can test the API endpoints using [Postman](https://www.postman.com/) or `curl`.

Example: **Load items into a box**

```http
POST http://localhost:8080/api/boxes/1/load-items
Content-Type: application/json

[
  {
    "name": "Item 1",
    "weight": 100,
    "code": "CODE_1"
  },
  {
    "name": "Item 2",
    "weight": 50,
    "code": "CODE_2"
  }
]
```

## Notes

- Default database is in-memory H2. Access the console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- Make sure `data.sql` is not present in `src/main/resources` unless you intend to seed data on every startup.

## Troubleshooting

- If you get errors about missing getters/setters, ensure annotation processing is enabled in your IDE for Lombok.
- If Maven CLI builds fail but IntelliJ works, see the Lombok section above and ensure your `pom.xml` is correct.
