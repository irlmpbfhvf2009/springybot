FROM openjdk:11

WORKDIR /app

COPY ./target/springybot-2.0.8.jar /app/springybot.jar

EXPOSE 5487

CMD ["java", "-jar", "/app/springybot.jar"]
