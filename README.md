# BazaarHub API

A production-ready Spring Boot 4.1.1 REST API for BazaarHub, designed to power marketplace and commerce workflows with a scalable, maintainable, and containerized backend.

## Overview

BazaarHub API provides a robust, enterprise-grade backend foundation for managing core marketplace operations including:
- product and catalog management
- vendor and customer operations
- order processing and transactions
- role-based authentication and authorization (customer, seller, admin)
- versioned database schema management with Flyway
- containerized deployment and local development environments

The application follows modern Spring Boot best practices, is fully containerized, and ready for production deployment.

## Features

- Spring Boot 4.1.1 with Java 21
- RESTful API with Spring Web
- Spring Data JPA with PostgreSQL
- Flyway versioned database migrations
- Spring Security with JWT token authentication
- Role-based access control (RBAC): customer, seller, admin
- Input validation with Bean Validation
- Reduced boilerplate with Lombok
- Docker Compose for local development
- Modular, scalable project architecture
- Test-ready structure with Spring Boot Test
- Production-ready error handling and logging

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Data JPA | 4.1.1 |
| Spring Security | 4.1.1 |
| PostgreSQL | 15+ |
| Flyway | 9.x+ |
| Lombok | 1.18.30+ |
| Docker | Latest |
| Docker Compose | Latest |

## Project Structure

```text
bazaarhub-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bazaarhub/
│   │   │           ├── BazaarHubApiApplication.java
│   │   │           ├── config/
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   └── JwtConfig.java
│   │   │           ├── controller/
│   │   │           │   ├── ProductController.java
│   │   │           │   ├── UserController.java
│   │   │           │   └── OrderController.java
│   │   │           ├── dto/
│   │   │           │   ├── request/
│   │   │           │   └── response/
│   │   │           ├── entity/
│   │   │           │   ├── Product.java
│   │   │           │   ├── User.java
│   │   │           │   ├── Order.java
│   │   │           │   └── BaseEntity.java
│   │   │           ├── exception/
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   └── custom exceptions
│   │   │           ├── repository/
│   │   │           │   ├── ProductRepository.java
│   │   │           │   ├── UserRepository.java
│   │   │           │   └── OrderRepository.java
│   │   │           ├── security/
│   │   │           │   ├── JwtTokenProvider.java
│   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │           │   └── CustomUserDetailsService.java
│   │   │           └── service/
│   │   │               ├── ProductService.java
│   │   │               ├── UserService.java
│   │   │               ├── AuthService.java
│   │   │               └── OrderService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── db/
│   │           └── migration/
│   │               ├── V1__Init_schema.sql
│   │               ├── V2__Create_users_table.sql
│   │               └── V3__Add_roles_and_permissions.sql
│   └── test/
│       └── java/
│           └── com/
│               └── bazaarhub/
│                   ├── controller/
│                   ├── service/
│                   └── repository/
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── LICENSE
```

## Prerequisites

Before running the application, ensure you have:

- **Java 21** or later
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **Git**

## Configuration

Application settings are managed through Spring Boot configuration files in `src/main/resources/`:

### application.properties (base configuration)

```properties
spring.application.name=bazaarhub-api
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/bazaarhub
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway (Database Migrations)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

# JWT Configuration
app.jwt.secret=your-secret-key-minimum-256-bits-length
app.jwt.expiration=86400000

# Logging
logging.level.root=INFO
logging.level.com.bazaarhub=DEBUG
```

### application-dev.properties (development profile)

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.com.bazaarhub=DEBUG
spring.h2.console.enabled=true
```

### application-prod.properties (production profile)

```properties
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.bazaarhub=INFO
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

## Running the Application

### Option 1: Local Development with Maven

```bash
# Clone the repository
git clone https://github.com/dheeraj-bartwal92/bazaarhub-api.git
cd bazaarhub-api

# Build the project
./mvnw clean install

# Run with Docker Compose (starts PostgreSQL)
docker-compose up -d

# Run the application
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Application starts at http://localhost:8080/api
```

### Option 2: Docker Compose (Recommended for Local Development)

```bash
# Start the entire stack (API + PostgreSQL)
docker-compose up --build

# Stop the stack
docker-compose down

# View logs
docker-compose logs -f app
```

### Option 3: Docker Container Only

```bash
# Build the Docker image
docker build -t bazaarhub-api:latest .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/bazaarhub \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  --name bazaarhub-api \
  bazaarhub-api:latest
```

## Docker Compose Setup

The `docker-compose.yml` manages local development with PostgreSQL:

```yaml
version: "3.9"

services:
  app:
    build: .
    container_name: bazaarhub-api
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: dev
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/bazaarhub
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      APP_JWT_SECRET: ${APP_JWT_SECRET:-your-default-secret-key}
    depends_on:
      - postgres
    networks:
      - bazaarhub-network
    healthcheck:
      test: [ "CMD", "curl", "-f", "http://localhost:8080/api/health" ]
      interval: 30s
      timeout: 10s
      retries: 3

  postgres:
    image: postgres:15-alpine
    container_name: bazaarhub-postgres
    environment:
      POSTGRES_DB: bazaarhub
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - bazaarhub-network
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U postgres" ]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
    driver: local

networks:
  bazaarhub-network:
    driver: bridge
```

## Database Migrations with Flyway

Database schema changes are version-controlled using Flyway. Migration files are located in `src/main/resources/db/migration/`:

### Migration File Naming Convention

```text
V<number>__<description>.sql
V1__Init_schema.sql
V2__Create_users_table.sql
V3__Add_roles_and_permissions.sql
```

### Example Migration: V1__Init_schema.sql

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    seller_id BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_seller_id ON products(seller_id);
CREATE INDEX idx_users_email ON users(email);
```

## Authentication & Authorization

### JWT Token-Based Authentication

The API uses JWT tokens for stateless authentication with role-based access control (RBAC):

**Supported Roles:**
- `CUSTOMER` — end customers, can browse and purchase products
- `SELLER` — vendors, can manage their products and orders
- `ADMIN` — system administrators, full access

### Authentication Flow

1. User calls `POST /api/auth/login` with credentials
2. Server returns a JWT token
3. Client includes token in `Authorization: Bearer <token>` header on subsequent requests
4. Server validates token and enforces role-based permissions

### Example Login Request

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "securepassword"
  }'
```

### Example Protected Endpoint Request

```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <jwt_token>"
```

## API Documentation

### Core Endpoints

#### Authentication
- `POST /api/auth/login` — User login, returns JWT token
- `POST /api/auth/register` — Register new user
- `POST /api/auth/refresh` — Refresh expired token

#### Products
- `GET /api/products` — List all products (public)
- `GET /api/products/{id}` — Get product details (public)
- `POST /api/products` — Create product (seller/admin)
- `PUT /api/products/{id}` — Update product (seller/admin)
- `DELETE /api/products/{id}` — Delete product (seller/admin)

#### Users
- `GET /api/users/{id}` — Get user profile (authenticated)
- `PUT /api/users/{id}` — Update user profile (authenticated)
- `GET /api/users` — List users (admin only)

#### Orders
- `POST /api/orders` — Create order (customer)
- `GET /api/orders/{id}` — Get order details (customer/admin)
- `GET /api/orders` — List user orders (customer)

## Input Validation

The application uses Bean Validation (Jakarta Validation) for input validation:

### Example DTO with Validation

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 2000)
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
    
    @NotNull(message = "Category is required")
    private String category;
}
```

## Testing

### Run All Tests

```bash
./mvnw test
```

### Run Specific Test Class

```bash
./mvnw test -Dtest=ProductServiceTest
```

### Run Tests with Coverage

```bash
./mvnw clean test jacoco:report
```

### Example Unit Test

```java
@SpringBootTest
class ProductServiceTest {
    
    @MockBean
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductService productService;
    
    @Test
    void testFindProductById() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        Product result = productService.findById(productId);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }
}
```

## Build & Deployment

### Build JAR Package

```bash
./mvnw clean package -DskipTests
```

The JAR will be generated at `target/bazaarhub-api-*.jar`

### Build Docker Image

```bash
docker build -t bazaarhub-api:latest .
docker tag bazaarhub-api:latest bazaarhub-api:v1.0.0
```

### Push to Container Registry

```bash
# Docker Hub
docker tag bazaarhub-api:latest your-username/bazaarhub-api:latest
docker push your-username/bazaarhub-api:latest

# AWS ECR, GCP, or other registries
docker tag bazaarhub-api:latest gcr.io/your-project/bazaarhub-api:latest
docker push gcr.io/your-project/bazaarhub-api:latest
```

### Production Deployment

The application can be deployed to:
- **Cloud Platforms:** AWS (ECS/Fargate), Google Cloud (Cloud Run), Azure (App Service)
- **Kubernetes:** Using Helm charts or native manifests
- **Traditional Servers:** VM or dedicated server with Docker runtime
- **CI/CD Pipelines:** GitHub Actions, GitLab CI, Jenkins, etc.

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | dev | Active Spring profile |
| `SPRING_DATASOURCE_URL` | jdbc:postgresql://localhost:5432/bazaarhub | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | postgres | Database username |
| `SPRING_DATASOURCE_PASSWORD` | postgres | Database password |
| `APP_JWT_SECRET` | (required) | JWT signing secret (min 256 bits) |
| `APP_JWT_EXPIRATION` | 86400000 | Token expiration time in milliseconds |

## Troubleshooting

### Database Connection Issues

```bash
# Verify PostgreSQL is running
docker ps | grep postgres

# Check logs
docker-compose logs postgres

# Connect to database
psql -U postgres -d bazaarhub -h localhost
```

### Flyway Migration Failures

```bash
# View Flyway logs
docker-compose logs app | grep Flyway

# Reset migrations (development only)
./mvnw flyway:clean flyway:migrate
```

### JWT Token Errors

- Ensure `APP_JWT_SECRET` is at least 256 bits
- Verify token is passed in `Authorization: Bearer <token>` format
- Check token expiration time in application properties

## Contributing

Contributions are welcome. To contribute:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make your changes and commit: `git commit -m "Add your feature"`
4. Push to your branch: `git push origin feature/your-feature`
5. Open a pull request

### Code Style

- Follow Google Java Style Guide
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Ensure all tests pass before submitting PR

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Support & Contact

For questions, issues, or collaboration inquiries:
- Open an issue on GitHub
- Contact the project maintainer

---

**Built with Spring Boot 4.1.1, Java 21, and containerized for modern deployment workflows.**
