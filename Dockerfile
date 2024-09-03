FROM maven:3.9.9-eclipse-temurin-17 as builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

COPY src/main/resources/image-storage /opt/app/image-storage

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]
