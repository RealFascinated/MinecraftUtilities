FROM fascinated/docker-images:java_17

RUN sudo apt install maven -y \
    && rm -rf /var/lib/apt/lists/*

RUN mvn clean package

CMD ["java", "-jar", "target/Minecraft-Helper-1.0-SNAPSHOT.jar"]