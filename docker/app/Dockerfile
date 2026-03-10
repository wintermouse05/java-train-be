FROM eclipse-temurin:17-jdk-jammy

# Install Maven, MySQL client, and netcat for connectivity checks.
RUN apt-get update && \
    apt-get install -y maven default-mysql-client netcat-traditional && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Maven wrapper and project descriptor first to leverage Docker layer caching.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and startup helper.
COPY src ./src
COPY docker/wait-for-mysql.sh /wait-for-mysql.sh
RUN sed -i 's/\r$//' /wait-for-mysql.sh && chmod +x /wait-for-mysql.sh

# Build once so dependencies/plugins are ready.
RUN mvn clean package -DskipTests

EXPOSE 8080

# Wait for MySQL, then execute the command below.
ENTRYPOINT ["/wait-for-mysql.sh", "mysql", "student", "Student@123", "student_db"]
CMD ["mvn", "spring-boot:run"]
