# Используем официальный образ OpenJDK в качестве базового
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл jar в контейнер
COPY target/your-spring-boot-app.jar /app/app.jar

# Указываем, какой порт будет прослушивать приложение внутри контейнера
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
