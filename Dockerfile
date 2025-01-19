FROM tomcat:10.1-jdk21
# Устанавливаем параметры запуска
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
# Устанавливаем рабочую директорию
WORKDIR /usr/local/tomcat/work/Catalina/localhost/ROOT
RUN mkdir -p uploads/tmp
WORKDIR /usr/local/tomcat/webapps

# Копируем ваш WAR файл в контейнер

COPY ./lib/libs/ /usr/local/tomcat/webapps/
RUN mv /usr/local/tomcat/webapps/web-1.0.0.war /usr/local/tomcat/webapps/ROOT.war
# Открываем порт 8080 для доступа
EXPOSE 8080
# Открываем порт для дебага
EXPOSE 8000
WORKDIR /usr/local/tomcat/bin
# Стартуем Tomcat
CMD ["catalina.sh", "run"]
