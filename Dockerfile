FROM openjdk:9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} scrapping-events.jar
ENTRYPOINT ["java","-jar","/scrapping-events.jar"]