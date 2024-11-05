# Используем официальный образ OpenJDK для Java 11 в качестве базового образа
FROM openjdk:23

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл вашего Spring Boot приложения с именем InventoryControl-0.0.1-SNAPSHOT.jar в контейнер
COPY target/InventoryControl-0.0.1-SNAPSHOT.jar /app/InventoryControl-0.0.1-SNAPSHOT.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8081

# Команда для запуска вашего Spring Boot приложения
CMD ["java", "-jar", "InventoryControl-0.0.1-SNAPSHOT.jar"]