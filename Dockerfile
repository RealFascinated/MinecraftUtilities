FROM maven:3.9.6-sapmachine-17

RUN apt install ttf-mscorefonts-installer -y && fc-cache -f

# Set the working directory
WORKDIR /home/container

# Copy the current directory contents into the container at /home/container
COPY . .

# Build the jar
RUN mvn package -q -Dmaven.test.skip -DskipTests -T2C

# Make port 80 available to the world outside this container
EXPOSE 80
ENV PORT=80

# Indicate that we're running in production
ENV ENVIRONMENT=production

# Run the jar file
CMD java -jar target/Minecraft-Utilities.jar -Djava.awt.headless=true