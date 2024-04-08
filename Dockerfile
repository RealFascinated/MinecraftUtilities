FROM fascinated/docker-images:java_17

COPY . .

RUN mvn package -q

EXPOSE 80
ENV PORT=80

CMD ["java", "-jar", "target/Minecraft-Helper-1.0-SNAPSHOT.jar"]