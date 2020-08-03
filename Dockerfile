#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app/pom.xml

RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/forumkafka-0.0.1-SNAPSHOT.jar /usr/local/lib/forumkafka.jar
COPY run.sh /home/app/run.sh
RUN chmod g+rwX /home/app/run.sh
RUN chmod g+rwX /usr/local/lib/forumkafka.jar
EXPOSE 9999
#ENTRYPOINT ["java", "-jar", "/usr/local/lib/forumkafka.jar"]