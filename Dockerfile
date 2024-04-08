FROM eclipse-temurin:17.0.10_7-jdk-focal

# Install maven
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR     /home/container

# Copy the current directory contents into the container at /home/container
COPY . .

# Build the jar
RUN mvn package -q

# Make port 80 available to the world outside this container
EXPOSE 80
ENV PORT=80

# Run the jar file
CMD ["java", "-jar", "target/Minecraft-Helper-1.0-SNAPSHOT.jar"]