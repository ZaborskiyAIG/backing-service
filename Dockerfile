FROM maven:3.8.3-openjdk-17
COPY target/backing-service.jar backing-service.jar
EXPOSE 5555
ENTRYPOINT ["java","-jar", "backing-service.jar"]