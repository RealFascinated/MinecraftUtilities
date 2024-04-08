FROM eclipse-temurin:17.0.10_7-jdk-focal

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y maven

COPY . .

RUN mvn package -q

EXPOSE 80
ENV PORT=80

CMD ["java", "-jar", "target/Minecraft-Helper-1.0-SNAPSHOT.jar"]