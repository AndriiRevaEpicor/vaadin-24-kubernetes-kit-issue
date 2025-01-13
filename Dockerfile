FROM openjdk:21-jdk-slim
COPY target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]