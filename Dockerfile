FROM tomcat:10.1-jdk21
# Устанавливаем параметры запуска
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000"
# Устанавливаем рабочую директорию для создания временного хранилища
WORKDIR /usr/local/tomcat/work/Catalina/localhost/ROOT
RUN mkdir -p uploads/tmp
# Устанавливаем рабочую директорию для создания папок с изображениями
WORKDIR /usr/local/tomcat/webapps
#RUN mkdir -p  ROOT/uploads/images
#COPY ROOT/WEB-INF/classes/uploads/images/ ROOT/uploads/images/
# Копируем  WAR файл в контейнер
COPY ./lib/libs/ /usr/local/tomcat/webapps/
COPY ./lib/resources/main /usr/local/tomcat/
RUN mv /usr/local/tomcat/webapps/web-1.0.war /usr/local/tomcat/webapps/ROOT.war
# Открываем порт 8080 для доступа
EXPOSE 8080
# Открываем порт для дебага
EXPOSE 8000
WORKDIR /usr/local/tomcat/bin
# Стартуем Tomcat
CMD ["catalina.sh", "run"]
