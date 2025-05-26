# Базовий образ із JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Робоча директорія
WORKDIR /app

# Копіюємо налаштування Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Копіюємо решту файлів і збираємо проєкт
COPY src ./src
RUN ./mvnw package -DskipTests -Dmaven.test.skip=true

# Вказуємо порт і команду запуску
EXPOSE 8080
CMD ["java", "-jar", "target/queue-management-0.0.1-SNAPSHOT.jar"]
