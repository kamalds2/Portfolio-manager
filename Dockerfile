# Multi-stage build for Spring Boot application
FROM eclipse-temurin:17-jdk AS builder


# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copy the built jar from builder stage (more explicit pattern)
COPY --from=builder /app/target/admin-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory with proper permissions
USER root
RUN mkdir -p /app/uploads && chown -R spring:spring /app/uploads
USER spring:spring

# Health check - optimized for Google Cloud Run
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Expose port (Google Cloud Run uses PORT environment variable)
EXPOSE 8080

# Environment variables for Google Cloud deployment
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
ENV SPRING_PROFILES_ACTIVE=gcp
ENV PORT=8080

# Run the application with Google Cloud optimizations
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT} -jar app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]