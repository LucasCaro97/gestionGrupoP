FROM openjdk:20
ADD target/gestion-0.0.1-SNAPSHOT.jar gestion-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/gestion-0.0.1-SNAPSHOT.jar"]