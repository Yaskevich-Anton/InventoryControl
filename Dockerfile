# Используем официальный образ Maven для сборки приложения
FROM maven:3.8.4-openjdk-11 AS build

# Устанавливаем директорию приложения в контейнере
WORKDIR /app

# Копируем файлы с зависимостями и pom.xml для ускорения сборки
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код приложения
COPY src ./src

# Собираем приложение
RUN mvn package

# Создаем отдельный контейнер для запуска приложения
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]