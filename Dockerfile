FROM openjdk:11

WORKDIR /app

COPY ./target/springybot-1.0.4.jar /app/springybot-1.0.4.jar

EXPOSE 5487

CMD ["java", "-jar", "/app/springybot-1.0.4.jar"]
