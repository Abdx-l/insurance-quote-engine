# Insurance Quote Engine

Spring Boot/JPA rating service with database-managed age factors, deterministic BigDecimal calculations, auditable factor breakdowns, and age-boundary tests. Rates are sample educational factors.

## Run

Java 17 and Maven: `mvn test && mvn spring-boot:run`. POST /quote with age, smoker, coverage and term. Default H2 data initializes age factors; configure DATABASE_URL/DB_USER/DB_PASSWORD for PostgreSQL.
