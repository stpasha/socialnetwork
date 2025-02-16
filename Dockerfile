FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/basebook-0.2.jar /app/basebook-0.2.jar

# Устанавливаем параметры запуска для JVM (например, для отладки)
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"

# Открываем порт для дебага
EXPOSE 8000

# Запуск приложения через java
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/basebook-0.2.jar"]
