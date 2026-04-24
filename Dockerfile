# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY pom.xml ./

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn dependency:go-offline -B


COPY src ./src
RUN mvn package -DskipTests -B

# ── Stage 2: Runtime ───────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre AS runtime

# Non-root user for security
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

# Create data directory for SQLite DB file and set ownership
RUN mkdir -p /app/data && chown -R appuser:appgroup /app

# Copy only the built artifact from the builder stage
COPY --from=builder /app/target/*.jar app.jar

USER appuser

VOLUME ["/app/data"]

EXPOSE ${SERVER_PORT:-8080}

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]