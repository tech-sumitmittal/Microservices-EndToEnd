#Start with a base image containing Java runtime
FROM openjdk:25-ea-33-oracle

# Add the application's jar to the image
COPY ./loans/target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]