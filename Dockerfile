FROM maven:3.8.7-eclipse-temurin-11

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
COPY testng.xml .
COPY config.properties .

CMD ["mvn", "test"]