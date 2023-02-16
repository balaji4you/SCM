FROM openjdk:8-jdk-alpine
EXPOSE 3001
RUN apk add maven
COPY . .
#ADD application.properties a.properties
#ADD /target/Funds_1199-0.0.1-SNAPSHOT.jar app.jar
RUN mvn clean install -q
ENTRYPOINT ["java","-jar","/target/AuthServerSecurity-0.0.1-SNAPSHOT.jar","--spring.config.location=classpath:file:application.properties"]
# testing for functioning/