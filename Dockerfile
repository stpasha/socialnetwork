FROM tomcat:10.1-jdk21
# Устанавливаем рабочую директорию
WORKDIR /usr/local/tomcat/webapps

# Копируем ваш WAR файл в контейнер

COPY ./lib/libs/ /usr/local/tomcat/webapps/
RUN mv /usr/local/tomcat/webapps/web-1.0.0.war /usr/local/tomcat/webapps/ROOT.war
# Открываем порт 8080 для доступа
EXPOSE 8080

# Стартуем Tomcat
CMD ["catalina.sh", "run"]
