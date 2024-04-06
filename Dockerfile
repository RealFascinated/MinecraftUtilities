FROM fascinated/docker-images:java_17

COPY . .

RUN mvn clean package

EXPOSE 7500

CMD ["java", "-jar", "target/Minecraft-Helper-1.0-SNAPSHOT.jar"]