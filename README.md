# Box Delivery Spring Boot API

## Build & Run

```
./mvnw clean install
./mvnw spring-boot:run
```

API runs at: `http://localhost:8080/api/boxes`

## Endpoints

- `POST /api/boxes` – Create a new box
- `POST /api/boxes/{id}/load` – Load items into a box
- `GET /api/boxes/{id}/items` – Get loaded items for a box
- `GET /api/boxes/available` – List boxes available for loading
- `GET /api/boxes/{id}/battery` – Check battery level

## Design Assumptions
- H2 in-memory DB for demo/testing.
- Business rules enforced in service layer.
- Items validated for naming and code format.
- Data preloaded using `data.sql`.
- 