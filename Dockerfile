FROM maven:3.8.5-openjdk-17-slim

# Set the working directory
WORKDIR /home/container

# Copy the current directory contents into the container at /home/container
COPY . .

# Build the jar
RUN mvn package -q -DskipTests

# Make port 80 available to the world outside this container
EXPOSE 80
ENV PORT=80

# Run the jar file
CMD ["java", "-jar", "target/Minecraft-Utilities.jar"]